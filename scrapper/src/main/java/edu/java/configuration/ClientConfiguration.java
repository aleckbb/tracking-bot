package edu.java.configuration;

import edu.java.clients.GitHubClient;
import edu.java.clients.StackOverFlowClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ClientConfiguration {
    @Bean("GitHubClient")
    public GitHubClient getGitHubClient(WebClient.Builder builder, @Value("${app.base-url-github}") String url) {
        return new GitHubClient(builder, url);
    }

    @Bean("StackOverFlowClient")
    public StackOverFlowClient getStackOverFlowClient(WebClient.Builder builder, @Value("${app.base-url-stackoverflow}") String url) {
        return new StackOverFlowClient(builder, url);
    }
}
