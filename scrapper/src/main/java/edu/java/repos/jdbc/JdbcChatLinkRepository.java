package edu.java.repos.jdbc;

import edu.java.dtoClasses.jdbc.DTOSub;
import edu.java.repos.interfaces.ChatLinkRepository;
import edu.java.repos.mappers.ChatLinkMapper;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class JdbcChatLinkRepository implements ChatLinkRepository {
    @Autowired
    private JdbcClient jdbcClient;

    @Transactional
    @Override
    public void add(DTOSub sub) {
        jdbcClient.sql("INSERT INTO chat_link VALUES(?, ?)")
            .params(
                sub.chatId(),
                sub.linkId()
            )
            .update();
    }

    @Transactional
    @Override
    public void remove(DTOSub sub) {
        jdbcClient.sql("DELETE FROM chat_link WHERE chat_id=? AND link_id=?")
            .params(
                sub.chatId(),
                sub.linkId()
            )
            .update();
    }

    @Transactional
    @Override
    public List<DTOSub> findAll() {
        return jdbcClient.sql("SELECT * FROM chat_link")
            .query(new ChatLinkMapper()).list();
    }

    @Transactional
    @Override
    public List<DTOSub> findByChatId(long chatId) {
        return jdbcClient.sql("SELECT * FROM chat_link WHERE chat_id=?")
            .param(chatId)
            .query(new ChatLinkMapper()).list();
    }

    @Transactional
    @Override
    public List<DTOSub> findByLinkId(long linkId) {
        return jdbcClient.sql("SELECT * FROM chat_link WHERE link_id=?")
            .param(linkId)
            .query(new ChatLinkMapper()).list();
    }
}
