package edu.java.configuration;

import edu.java.repos.jpa.JpaChatRepository;
import edu.java.repos.jpa.JpaLinkRepository;
import edu.java.service.handlers.GitHubHandler;
import edu.java.service.handlers.SofHandler;
import edu.java.service.interfaces.ChatService;
import edu.java.service.interfaces.LinkService;
import edu.java.service.interfaces.LinkUpdater;
import edu.java.service.jpa.JpaChatService;
import edu.java.service.jpa.JpaLinkService;
import edu.java.service.jpa.JpaLinkUpdater;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
public class JpaAccessConfiguration {
    @Bean
    public ChatService chatService(
        JpaChatRepository jpaChatRepository,
        JpaLinkRepository jpaLinkRepository
    ) {
        return new JpaChatService(jpaChatRepository, jpaLinkRepository);
    }

    @Bean
    public LinkService linkService(
        JpaLinkRepository jpaLinkRepository,
        JpaChatRepository jpaChatRepository,
        GitHubHandler gitHubHandler,
        SofHandler sofHandler
    ) {
        return new JpaLinkService(jpaLinkRepository, jpaChatRepository, gitHubHandler, sofHandler);
    }

    @Bean
    public LinkUpdater linkUpdater(
        JpaLinkRepository jpaLinkRepository
    ) {
        return new JpaLinkUpdater(jpaLinkRepository);
    }
}
