package edu.java.scrapper.jooq;

import edu.java.scrapper.IntegrationTest;
import edu.java.scrapper.dtoClasses.jdbc.DTOChat;
import edu.java.scrapper.repos.jdbc.JdbcChatRepository;
import edu.java.scrapper.repos.jooq.JooqChatRepository;
import org.junit.jupiter.api.BeforeAll;
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
public class JooqChatRepositoryTest extends IntegrationTest {
    @Autowired
    private JooqChatRepository chatRepository;

    static DTOChat chat;

    @BeforeAll
    static void createDto() {
        chat = new DTOChat(
            1L,
            "Alexey",
            OffsetDateTime.parse("2022-01-01T10:30:00+00:00")
        );
    }

    @Test
    @Transactional
    @Rollback
    void add() {
        assertEquals(0, chatRepository.findAll().size());
        chatRepository.add(chat);
        assertEquals("Alexey", chatRepository.findAll().getFirst().name());
        assertEquals(1, chatRepository.findAll().size());
    }

    @Test
    @Transactional
    @Rollback
    void remove() {
        chatRepository.add(chat);
        assertEquals(1, chatRepository.findAll().size());
        chatRepository.remove(chat);
        assertEquals(0, chatRepository.findAll().size());
    }

    @Test
    @Transactional
    @Rollback
    void findAll() {
        chatRepository.add(chat);
        assertEquals(1, chatRepository.findAll().size());
        assertEquals(
            "[DTOChat[chatId=1, name=Alexey, createdAt=2022-01-01T10:30Z]]",
            chatRepository.findAll().toString()
        );
    }
}
