package edu.java.configuration;

import edu.java.botclient.BotClient;
import edu.java.clients.GitHubClient;
import edu.java.clients.StackOverflowClient;
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
    public StackOverflowClient getStackOverFlowClient(
        WebClient.Builder builder,
        @Value("${app.base-url-stackoverflow}") String url
    ) {
        return new StackOverflowClient(builder, url);
    }

    @Bean("BotClient")
    public BotClient getBotClient(
        WebClient.Builder builder,
        @Value("${base-url-bot}") String url
    ) {
        return new BotClient(builder, url);
    }
}
