package edu.java.scrapper.configuration;

import edu.java.scrapper.repos.jooq.JooqChatLinkRepository;
import edu.java.scrapper.repos.jooq.JooqChatRepository;
import edu.java.scrapper.repos.jooq.JooqLinkRepository;
import edu.java.scrapper.service.handlers.GitHubHandler;
import edu.java.scrapper.service.handlers.SofHandler;
import edu.java.scrapper.service.interfaces.ChatService;
import edu.java.scrapper.service.interfaces.LinkService;
import edu.java.scrapper.service.interfaces.LinkUpdater;
import edu.java.scrapper.service.jooq.JooqChatService;
import edu.java.scrapper.service.jooq.JooqLinkService;
import edu.java.scrapper.service.jooq.JooqLinkUpdater;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jooq")
public class JooqAccessConfiguration {
    @Bean
    public ChatService chatService(
        JooqChatRepository chatRepository,
        JooqLinkRepository linkRepository,
        JooqChatLinkRepository chatLinkRepository
    ) {
        return new JooqChatService(chatRepository, chatLinkRepository, linkRepository);
    }

    @Bean
    public LinkService linkService(
        JooqChatRepository chatRepository,
        JooqLinkRepository linkRepository,
        JooqChatLinkRepository chatLinkRepository,
        GitHubHandler gitHubHandler,
        SofHandler sofHandler
    ) {
        return new JooqLinkService(chatRepository, chatLinkRepository, linkRepository, gitHubHandler, sofHandler);
    }

    @Bean
    public LinkUpdater linkUpdater(
        JooqLinkRepository linkRepository,
        JooqChatLinkRepository chatLinkRepository
    ) {
        return new JooqLinkUpdater(chatLinkRepository, linkRepository);
    }
}
