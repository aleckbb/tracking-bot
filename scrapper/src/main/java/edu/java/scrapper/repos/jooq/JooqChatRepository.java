package edu.java.scrapper.repos.jooq;

import edu.java.scrapper.dtoClasses.jdbc.DTOChat;
import edu.java.scrapper.repos.interfaces.ChatRepository;
import edu.java.scrapper.repos.jooq.generated.tables.Chat;
import java.util.List;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class JooqChatRepository implements ChatRepository {
    @Autowired DSLContext context;

    @Transactional
    @Override
    public void add(DTOChat chat) {
        context.insertInto(Chat.CHAT)
            .values(chat.chatId(), chat.name(), chat.createdAt())
            .execute();
    }

    @Transactional
    @Override
    public void remove(DTOChat chat) {
        context.deleteFrom(Chat.CHAT)
            .where(Chat.CHAT.CHAT_ID.eq(chat.chatId()))
            .execute();
    }

    @Transactional
    @Override
    public List<DTOChat> findAll() {
        return context.selectFrom(Chat.CHAT)
            .fetchInto(DTOChat.class);
    }

    @Transactional
    @Override
    public Boolean existsById(long chatId) {
        return context.fetchOne(Chat.CHAT, Chat.CHAT.CHAT_ID.eq(chatId)) != null;
    }
}
