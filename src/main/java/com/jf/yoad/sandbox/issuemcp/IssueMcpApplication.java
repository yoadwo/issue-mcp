package com.jf.yoad.sandbox.issuemcp;

import com.jf.yoad.sandbox.issuemcp.models.ProjectKeys;
import com.jf.yoad.sandbox.issuemcp.services.JiraIssueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@Slf4j
public class IssueMcpApplication implements CommandLineRunner {

    private final JiraIssueService jiraIssueService;

    public static void main(String[] args) {
        SpringApplication.run(IssueMcpApplication.class, args);
    }

    public IssueMcpApplication(JiraIssueService jiraIssueService) {
        this.jiraIssueService = jiraIssueService;
    }

    @Override
    public void run(String... args) throws Exception {
        String freeText = """
                This is a test issue created using the Jira SDK.
                It contains a code block and some free text.
                """;
        String codeBlock = """
                public static void main(String[] args) {
                    SpringApplication.run(IssueMcpApplication.class, args);
                    }
                """;
        var createdIssue = jiraIssueService.createIssue(
                ProjectKeys.RTFS.name(), "created using jira sdk",
                freeText, codeBlock);
        if (createdIssue != null) {
            log.info("Issue created successfully: https://jfrog-int.atlassian.net/browse/{}",createdIssue.getKey());
        } else {
            log.warn("Failed to create issue.");
        }
    }
}
