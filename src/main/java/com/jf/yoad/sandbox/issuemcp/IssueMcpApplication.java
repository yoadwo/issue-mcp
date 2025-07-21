package com.jf.yoad.sandbox.issuemcp;

import com.jf.yoad.sandbox.issuemcp.services.JiraIssueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;


@SpringBootApplication
@Slf4j
public class IssueMcpApplication /*implements CommandLineRunner*/ {

    public static void main(String[] args) {
        SpringApplication.run(IssueMcpApplication.class, args);
    }

    @Bean
    public List<ToolCallback> jiraTools(JiraIssueService jiraIssueService) {
        return List.of(ToolCallbacks.from(jiraIssueService));
    }
}
