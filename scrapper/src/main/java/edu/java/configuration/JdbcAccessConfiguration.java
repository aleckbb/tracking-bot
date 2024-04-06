package edu.java.configuration;

import edu.java.repos.jdbc.JdbcChatLinkRepository;
import edu.java.repos.jdbc.JdbcChatRepository;
import edu.java.repos.jdbc.JdbcLinkRepository;
import edu.java.service.handlers.GitHubHandler;
import edu.java.service.handlers.SofHandler;
import edu.java.service.interfaces.ChatService;
import edu.java.service.interfaces.LinkService;
import edu.java.service.interfaces.LinkUpdater;
import edu.java.service.jdbc.JdbcChatService;
import edu.java.service.jdbc.JdbcLinkService;
import edu.java.service.jdbc.JdbcLinkUpdater;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jdbc")
public class JdbcAccessConfiguration {
    @Bean
    public ChatService chatService(
        JdbcChatRepository chatRepository,
        JdbcLinkRepository linkRepository,
        JdbcChatLinkRepository chatLinkRepository
    ) {
        return new JdbcChatService(chatRepository, linkRepository, chatLinkRepository);
    }

    @Bean
    public LinkService linkService(
        JdbcChatRepository chatRepository,
        JdbcLinkRepository linkRepository,
        JdbcChatLinkRepository chatLinkRepository,
        GitHubHandler gitHubHandler,
        SofHandler sofHandler
    ) {
        return new JdbcLinkService(chatRepository, linkRepository, chatLinkRepository, gitHubHandler, sofHandler);
    }

    @Bean
    public LinkUpdater linkUpdater(
        JdbcLinkRepository linkRepository,
        JdbcChatLinkRepository chatLinkRepository
    ) {
        return new JdbcLinkUpdater(linkRepository, chatLinkRepository);
    }
}
