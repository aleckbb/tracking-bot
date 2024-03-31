package edu.java.scrapper.jdbc;

import edu.java.dtoClasses.jdbc.DTOChat;
import edu.java.dtoClasses.jdbc.DTOLink;
import edu.java.dtoClasses.jdbc.DTOSub;
import edu.java.repos.jdbc.ChatRepositoryImpl;
import edu.java.repos.jdbc.ChatLinkRepositoryImpl;
import edu.java.repos.jdbc.LinkRepositoryImpl;
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
    private ChatRepositoryImpl chatRepository;
    @Autowired
    private LinkRepositoryImpl linkRepository;
    @Autowired
    private ChatLinkRepositoryImpl chatLinkRepository;

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
        chatLinkRepository.add(new DTOSub(chat.chatId(), linkRepository.findByUrl(link.url()).linkId()));
        assertEquals(chat.chatId(), chatLinkRepository.findAll().getFirst().chatId());
        assertEquals(linkRepository.findByUrl(link.url()).linkId(), chatLinkRepository.findAll().getFirst().linkId());
        assertEquals(1, chatRepository.findAll().size());
    }

    @Test
    @Transactional
    @Rollback
    void remove() {
        linkRepository.add(link);
        chatRepository.add(chat);
        sub = new DTOSub(chat.chatId(), linkRepository.findByUrl(link.url()).linkId());
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
        sub = new DTOSub(chat.chatId(), linkRepository.findByUrl(link.url()).linkId());
        chatLinkRepository.add(sub);
        assertEquals(1, chatLinkRepository.findAll().size());
        assertEquals(
            "[DTOSub[chatId=1, linkId=" + linkRepository.findByUrl(link.url()).linkId() + "]]",
            chatLinkRepository.findAll().toString()
        );
    }
}
