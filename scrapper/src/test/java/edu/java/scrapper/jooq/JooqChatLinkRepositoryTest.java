package edu.java.scrapper.jooq;

import edu.java.scrapper.IntegrationTest;
import edu.java.scrapper.dtoClasses.jdbc.DTOChat;
import edu.java.scrapper.dtoClasses.jdbc.DTOLink;
import edu.java.scrapper.dtoClasses.jdbc.DTOSub;
import edu.java.scrapper.repos.jdbc.JdbcChatLinkRepository;
import edu.java.scrapper.repos.jdbc.JdbcChatRepository;
import edu.java.scrapper.repos.jdbc.JdbcLinkRepository;
import edu.java.scrapper.repos.jooq.JooqChatLinkRepository;
import edu.java.scrapper.repos.jooq.JooqChatRepository;
import edu.java.scrapper.repos.jooq.JooqLinkRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import java.time.OffsetDateTime;
import static org.junit.Assert.assertEquals;

@SpringBootTest
@DirtiesContext
public class JooqChatLinkRepositoryTest extends IntegrationTest {
    @Autowired
    private JooqChatRepository chatRepository;
    @Autowired
    private JooqLinkRepository linkRepository;
    @Autowired
    private JooqChatLinkRepository chatLinkRepository;

    private final OffsetDateTime time = OffsetDateTime.parse("2022-01-01T10:30:00+00:00");
    private final DTOChat chat = new DTOChat(
        1L,
        "Alexey",
        time
    );
    private final DTOLink link = new DTOLink(
        1L,
        "https://test",
        time,
        time,
        "",
        ""
    );
    private DTOSub sub;

    @Test
    @Transactional
    @Rollback
    void add() {
        linkRepository.add(link);
        chatRepository.add(chat);
        assertEquals(0, chatLinkRepository.findAll().size());
        chatLinkRepository.add(new DTOSub(chat.chatId(), linkRepository.findByUrl(link.getUrl()).getLinkId()));
        assertEquals(chat.chatId(), chatLinkRepository.findAll().getFirst().chatId());
        assertEquals(linkRepository.findByUrl(link.getUrl()).getLinkId(), chatLinkRepository.findAll().getFirst().linkId());
        assertEquals(1, chatRepository.findAll().size());
    }

    @Test
    @Transactional
    @Rollback
    void remove() {
        linkRepository.add(link);
        chatRepository.add(chat);
        sub = new DTOSub(chat.chatId(), linkRepository.findByUrl(link.getUrl()).getLinkId());
        chatLinkRepository.add(sub);
        assertEquals(1, chatLinkRepository.findAll().size());
        chatLinkRepository.remove(sub);
        assertEquals(0, chatLinkRepository.findAll().size());
    }

    @Test
    @Transactional
    @Rollback
    void findAll() {
        linkRepository.add(link);
        chatRepository.add(chat);
        sub = new DTOSub(chat.chatId(), linkRepository.findByUrl(link.getUrl()).getLinkId());
        chatLinkRepository.add(sub);
        assertEquals(1, chatLinkRepository.findAll().size());
        assertEquals(
            "[DTOSub[chatId=1, linkId=" + linkRepository.findByUrl(link.getUrl()).getLinkId() + "]]",
            chatLinkRepository.findAll().toString()
        );
    }
}
