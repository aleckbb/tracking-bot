package edu.java.scrapper.jdbc;

import edu.java.dtoClasses.jdbc.DTOChat;
import edu.java.repos.chat.ChatRepositoryImpl;
import edu.java.scrapper.IntegrationTest;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.Assert.assertEquals;

@SpringBootTest
public class ChatRepositoryImplTest extends IntegrationTest {
    @Autowired
    private ChatRepositoryImpl chatRepository;

    static DTOChat chat;

    @BeforeAll
    static void createDto() {
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
            "[DTOChat[chatId=1, name=Alexey, createdAt=2024-03-17T21:31Z]]",
            chatRepository.findAll().toString()
        );
    }
}
