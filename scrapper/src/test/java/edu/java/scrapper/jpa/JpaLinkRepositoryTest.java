package edu.java.scrapper.jpa;

import edu.java.scrapper.IntegrationTest;
import edu.java.scrapper.repos.jpa.JpaChatRepository;
import edu.java.scrapper.repos.jpa.JpaLinkRepository;
import edu.java.scrapper.repos.jpa.entities.Chat;
import edu.java.scrapper.repos.jpa.entities.Link;
import java.time.OffsetDateTime;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.Assert.assertEquals;

@SpringBootTest
class JpaLinkRepositoryTest extends IntegrationTest {
    @Autowired private JpaLinkRepository jpaLinkRepository;
    @Autowired private JpaChatRepository jpaChatRepository;

    static Link link;
    static Chat chat;

    @BeforeAll static void createDto() {
        chat = new Chat(
            1L,
            OffsetDateTime.parse("2022-01-01T10:30:00+00:00"),
            "Alexey"
        );
        link = new Link(
            "https://test",
            OffsetDateTime.parse("2022-01-01T10:30:00+00:00"),
            OffsetDateTime.parse("2022-01-01T10:30:00+00:00"),
            "",
            "",
            Set.of(chat)
        );
    }

    @Test @Transactional @Rollback void add() {
        jpaChatRepository.saveAndFlush(chat);
        assertEquals(0, jpaLinkRepository.findAll().size());
        jpaLinkRepository.saveAndFlush(link);
        assertEquals("https://test", jpaLinkRepository.findAll().getFirst().getUrl());
        assertEquals(1, jpaLinkRepository.findAll().size());
    }

    @Test @Transactional @Rollback void remove() {
        jpaChatRepository.saveAndFlush(chat);
        jpaLinkRepository.saveAndFlush(link);
        assertEquals(1, jpaLinkRepository.findAll().size());
        jpaLinkRepository.deleteById(jpaLinkRepository.findByUrl(link.getUrl()).getLinkId());
        assertEquals(0, jpaLinkRepository.findAll().size());
    }

    @Test @Transactional @Rollback void findAll() {
        jpaChatRepository.saveAndFlush(chat);
        jpaLinkRepository.saveAndFlush(link);
        assertEquals(1, jpaLinkRepository.findAll().size());
        assertEquals(
            "[Link(linkId="
                + jpaLinkRepository.findByUrl(link.getUrl()).getLinkId()
                + ", url=https://test, updateAt=2022-01-01T10:30Z, "
                + "checkAt=2022-01-01T10:30Z, linkType=, data=,"
                + " chats=[Chat(chatId=1, name=Alexey, createdAt=2022-01-01T10:30Z, links=[])])]",
            jpaLinkRepository.findAll().toString()
        );
    }
}
