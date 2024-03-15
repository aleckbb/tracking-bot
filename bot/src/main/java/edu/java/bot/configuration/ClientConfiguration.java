package edu.java.bot.configuration;

import edu.java.bot.scrapperclient.ScrapperClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ClientConfiguration {
    @Bean("ScrapperClient")
    public ScrapperClient getScrapperClient(
        WebClient.Builder builder,
        @Value("${base-url-scrapper}") String url) {
        return new ScrapperClient(builder, url);
    }
}
