package edu.java.bot.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ClientConfiguration {
    @Bean("ScrapperClient")
    public WebClient getScrapperClient(@Value("${base-url-scrapper}") String url) {
        return WebClient.builder().baseUrl(url).build();
    }
}
