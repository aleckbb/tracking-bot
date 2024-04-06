package edu.java.repos.jdbc;

import edu.java.dtoClasses.jdbc.DTOChat;
import edu.java.repos.interfaces.ChatRepository;
import edu.java.repos.mappers.ChatMapper;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class JdbcChatRepository implements ChatRepository {

    @Autowired
    private JdbcClient jdbcClient;

    @Transactional
    @Override
    public void add(DTOChat chat) {
        jdbcClient.sql("INSERT INTO chat VALUES(?, ?, ?)")
            .params(
                chat.chatId(),
                chat.name(),
                chat.createdAt()
            )
            .update();
    }

    @Transactional
    @Override
    public void remove(DTOChat chat) {
        jdbcClient.sql("DELETE FROM chat WHERE chat_id=?")
            .param(chat.chatId())
            .update();
    }

    @Transactional
    @Override
    public List<DTOChat> findAll() {
        return jdbcClient.sql("SELECT * FROM chat")
            .query(new ChatMapper()).list();
    }
}
