package edu.java.scrapper.configuration;

import edu.java.scrapper.repos.jpa.JpaChatRepository;
import edu.java.scrapper.repos.jpa.JpaLinkRepository;
import edu.java.scrapper.service.handlers.GitHubHandler;
import edu.java.scrapper.service.handlers.SofHandler;
import edu.java.scrapper.service.interfaces.ChatService;
import edu.java.scrapper.service.interfaces.LinkService;
import edu.java.scrapper.service.interfaces.LinkUpdater;
import edu.java.scrapper.service.jpa.JpaChatService;
import edu.java.scrapper.service.jpa.JpaLinkService;
import edu.java.scrapper.service.jpa.JpaLinkUpdater;
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
