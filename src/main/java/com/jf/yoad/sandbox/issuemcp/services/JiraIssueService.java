package com.jf.yoad.sandbox.issuemcp.services;

import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.domain.BasicIssue;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.input.IssueInput;
import com.atlassian.jira.rest.client.api.domain.input.IssueInputBuilder;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import com.jf.yoad.sandbox.issuemcp.models.IssueTypes;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@Service
@Slf4j
public class JiraIssueService {

    @Value("${jira.server.url}")
    private String jiraServerUrl;

    @Value("${jira.server.username}")
    private String jiraUsername;

    @Value("${jira.server.password}")
    private String jiraPassword; // Consider using an API Token for better security if Jira Cloud

    private JiraRestClient restClient;

    @PostConstruct
    public void init() throws URISyntaxException {
        try {
            log.info("Initializing JiraRestClient for URL: {}", jiraServerUrl);
            URI jiraUri = new URI(jiraServerUrl);
            this.restClient = new AsynchronousJiraRestClientFactory()
                    .createWithBasicHttpAuthentication(jiraUri, jiraUsername, jiraPassword);
            log.info("JiraRestClient initialized successfully.");
        } catch (URISyntaxException e) {
            log.error("Invalid Jira Server URL: {}", jiraServerUrl, e);
            throw e;
        }
    }

    @PreDestroy
    public void destroy() {
        if (restClient != null) {
            try {
                restClient.close();
                log.info("JiraRestClient closed successfully.");
            } catch (IOException e) {
                log.error("Error closing JiraRestClient: {}", e.getMessage(), e);
            }
        }
    }

    /**
     * Retrieves a Jira issue by its key or ID.
     * @param issueKey The key (e.g., "PROJ-123") or ID of the issue.
     * @return The Issue object, or null if not found or an error occurs.
     */
    public Issue getIssueById(String issueKey) {
        if (restClient == null) {
            log.error("JiraRestClient is not initialized. Cannot retrieve issue.");
            return null;
        }
        try {
            log.info("Attempting to retrieve Jira issue: {}", issueKey);
            // .claim() blocks until the Promise is fulfilled or rejected.
            // For production, consider handling Promises asynchronously.
            Issue issue = restClient.getIssueClient().getIssue(issueKey).claim();
            log.info("Successfully retrieved issue {}: {}", issue.getKey(), issue.getSummary());
            return issue;
        } catch (Exception e) {
            log.error("Error retrieving issue {}: {}", issueKey, e.getMessage(), e);
            return null;
        }
    }

    /**
     * Create a jira issue.
     * Description markdown example:
     *
     *  {noformat}codeblock1
     * codeblock2{noformat}
     * single ticket {{text}}
     */
    @Tool(name = "create_jira_issue", description = """
    Creates a Jira issue in the specified project with a summary (title), and description.
    The description will include a free text and the highlighted code block (from the user).
    """)
    public BasicIssue createIssue(String projectKey, String summary,
                                  String freeText, String codeblock) {
        if (restClient == null) {
            log.error("JiraRestClient is not initialized. Cannot create issue.");
            return null;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(freeText);
        sb.append("\n\n");
        sb.append("{noformat}");
        sb.append(codeblock);
        sb.append("{noformat}");
        sb.append("\n\n");

        log.info("Attempting to create Jira issue in project {}: {}", projectKey, summary);
        // Create an issue input builder
        IssueInputBuilder issueInputBuilder = new IssueInputBuilder();
        issueInputBuilder
                .setProjectKey(projectKey)
                .setSummary(summary)
                .setDescription(sb.toString())
                .setIssueTypeId(IssueTypes.TASK.id); // 10000 is typically "Task" - adjust as needed
        IssueInput issueInput = issueInputBuilder.build();

        try {
            // Create the issue and block until completion with claim()
            BasicIssue newIssue = restClient.getIssueClient().createIssue(issueInput).claim();
            log.info("Successfully created issue: {}", newIssue.getKey());
            return newIssue;
        } catch (Exception e) {
            log.error("Error creating issue in project {}: {}", projectKey, e.getMessage(), e);
            return null;
        }
    }
}
