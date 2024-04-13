package edu.java.scrapper.jpa;

import edu.java.scrapper.IntegrationTest;
import edu.java.scrapper.repos.jpa.JpaChatRepository;
import edu.java.scrapper.repos.jpa.entities.Chat;
import java.time.OffsetDateTime;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.Assert.assertEquals;

@SpringBootTest
public class JpaChatRepositoryTest extends IntegrationTest {
    @Autowired
    private JpaChatRepository jpaChatRepository;

    static Chat chat;

    @BeforeAll
    static void createChat() {
        chat = new Chat(
            1L,
            OffsetDateTime.parse("2022-01-01T10:30:00+00:00"),
            "Alexey"
        );
    }

    @Test
    @Transactional
    @Rollback
    void add() {
        assertEquals(0, jpaChatRepository.findAll().size());
        jpaChatRepository.saveAndFlush(chat);
        assertEquals("Alexey", jpaChatRepository.findAll().getFirst().getName());
        assertEquals(1, jpaChatRepository.findAll().size());
    }

    @Test
    @Transactional
    @Rollback
    void remove() {
        jpaChatRepository.saveAndFlush(chat);
        assertEquals(1, jpaChatRepository.findAll().size());
        jpaChatRepository.delete(chat);
        jpaChatRepository.flush();
        assertEquals(0, jpaChatRepository.findAll().size());
    }

    @Test
    @Transactional
    @Rollback
    void findAll() {
        jpaChatRepository.saveAndFlush(chat);
        assertEquals(1, jpaChatRepository.findAll().size());
        assertEquals(
            "[Chat(chatId=1, name=Alexey, createdAt=2022-01-01T10:30Z, links=[])]",
            jpaChatRepository.findAll().toString()
        );
    }

}
