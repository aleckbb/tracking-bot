package edu.java.scrapper.jdbc;

import edu.java.dtoClasses.jdbc.DTOChat;
import edu.java.dtoClasses.jdbc.DTOLink;
import edu.java.dtoClasses.jdbc.DTOSub;
import edu.java.repos.chat.ChatRepositoryImpl;
import edu.java.repos.chatLink.ChatLinkRepositoryImpl;
import edu.java.repos.link.LinkRepositoryImpl;
import edu.java.repos.mappers.ChatLinkMapper;
import edu.java.scrapper.IntegrationTest;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.Assert.assertEquals;

public class ChatLinkRepositoryImplTest extends IntegrationTest {
    @Autowired
    private ChatRepositoryImpl chatRepository;
    @Autowired
    private LinkRepositoryImpl linkRepository;
    @Autowired
    private ChatLinkRepositoryImpl chatLinkRepository;

    @Autowired
    private JdbcClient jdbcClient;

    static DTOChat chat;
    static DTOLink link;
    static DTOSub sub;

    @BeforeAll
    static void createDto() {
        link = new DTOLink(
            1L,
            "https://test",
            OffsetDateTime.of(
                2024,
                3,
                17,
                18,
                31,
                0,
                0,
                ZoneOffset.UTC
            ),
            OffsetDateTime.of(
                2024,
                3,
                17,
                19,
                11,
                0,
                0,
                ZoneOffset.UTC
            ),
            "",
            ""
        );
        chat = new DTOChat(
            1L,
            "Alexey",
            OffsetDateTime.of(
                2024,
                3,
                17,
                18,
                31,
                0,
                0,
                ZoneOffset.UTC
            )
        );
    }

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
        var expected = jdbcClient.sql("SELECT * FROM chat_link")
            .query(new ChatLinkMapper()).list();
        assertEquals(
            expected.toString(),
            chatLinkRepository.findAll().toString()
        );
    }
}
