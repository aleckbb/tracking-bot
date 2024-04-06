package edu.java.scrapper.jdbc;

import edu.java.dtoClasses.jdbc.DTOChat;
import edu.java.dtoClasses.jdbc.DTOLink;
import edu.java.dtoClasses.jdbc.DTOSub;
import edu.java.repos.jdbc.JdbcChatRepository;
import edu.java.repos.jdbc.JdbcChatLinkRepository;
import edu.java.repos.jdbc.JdbcLinkRepository;
import edu.java.scrapper.IntegrationTest;
import java.time.OffsetDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.Assert.assertEquals;

@SpringBootTest
public class ChatLinkRepositoryImplTest extends IntegrationTest {
    @Autowired
    private JdbcChatRepository chatRepository;
    @Autowired
    private JdbcLinkRepository linkRepository;
    @Autowired
    private JdbcChatLinkRepository chatLinkRepository;

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
