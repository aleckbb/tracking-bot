package edu.java.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ClientConfiguration {
    @Bean("GitHubClient")
    public WebClient getGitHubClient(@Value("${app.base-url-github}") String url) {
        return WebClient.builder().baseUrl(url).build();
    }

    @Bean("StackOverFlowClient")
    public WebClient getStackOverFlowClient(@Value("${app.base-url-stackoverflow}") String url) {
        return WebClient.builder().baseUrl(url).build();
    }

    @Bean("BotClient")
    public WebClient getBotClient(@Value("${base-url-bot}") String url) {
        return WebClient.builder().baseUrl(url).build();
    }
}
