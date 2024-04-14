package edu.java.scrapper.configuration;

import edu.java.models.Request.LinkUpdate;
import edu.java.scrapper.linkUpdateService.BotClient;
import edu.java.scrapper.clients.GitHubClient;
import edu.java.scrapper.clients.StackOverflowClient;
import edu.java.scrapper.linkUpdateService.ScrapperQueueProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
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

    @Bean
    @ConditionalOnProperty(prefix = "app", name = "use-queue", havingValue = "false")
    public BotClient getBotClient(
        WebClient.Builder builder,
        @Value("${base-url-bot}") String url
    ) {
        return new BotClient(builder, url);
    }

    @Bean
    @ConditionalOnProperty(prefix = "app", name = "use-queue", havingValue = "true")
    public ScrapperQueueProducer getScrapperQueueProducer(
        KafkaTemplate<String, LinkUpdate> kafkaTemplate,
        ApplicationConfig applicationConfig) {
        return new ScrapperQueueProducer(kafkaTemplate, applicationConfig.kafka().topicName());
    }
}
