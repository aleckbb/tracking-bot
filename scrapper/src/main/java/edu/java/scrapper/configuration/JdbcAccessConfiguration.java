package edu.java.scrapper.configuration;

import edu.java.scrapper.repos.jdbc.JdbcChatLinkRepository;
import edu.java.scrapper.repos.jdbc.JdbcChatRepository;
import edu.java.scrapper.repos.jdbc.JdbcLinkRepository;
import edu.java.scrapper.service.handlers.GitHubHandler;
import edu.java.scrapper.service.handlers.SofHandler;
import edu.java.scrapper.service.interfaces.ChatService;
import edu.java.scrapper.service.interfaces.LinkService;
import edu.java.scrapper.service.interfaces.LinkUpdater;
import edu.java.scrapper.service.jdbc.JdbcChatService;
import edu.java.scrapper.service.jdbc.JdbcLinkService;
import edu.java.scrapper.service.jdbc.JdbcLinkUpdater;
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
