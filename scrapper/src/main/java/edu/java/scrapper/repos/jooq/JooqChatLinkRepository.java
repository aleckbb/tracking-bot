package edu.java.scrapper.repos.jooq;

import edu.java.scrapper.dtoClasses.jdbc.DTOSub;
import edu.java.scrapper.repos.interfaces.ChatLinkRepository;
import edu.java.scrapper.repos.jooq.generated.tables.ChatLink;
import java.util.List;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class JooqChatLinkRepository implements ChatLinkRepository {

    @Autowired DSLContext context;

    @Transactional
    @Override
    public void add(DTOSub sub) {
        context.insertInto(ChatLink.CHAT_LINK)
            .values(sub.chatId(), sub.linkId())
            .execute();
    }

    @Transactional
    @Override
    public void remove(DTOSub sub) {
        context.deleteFrom(ChatLink.CHAT_LINK)
            .where(
                ChatLink.CHAT_LINK.CHAT_ID.eq(sub.chatId())
                    .and(ChatLink.CHAT_LINK.LINK_ID.eq(sub.linkId())))
            .execute();

    }

    @Transactional
    @Override
    public List<DTOSub> findAll() {
        return context.selectFrom(ChatLink.CHAT_LINK)
            .fetchInto(DTOSub.class);
    }

    @Transactional
    @Override
    public List<DTOSub> findByChatId(long chatId) {
        return context.selectFrom(ChatLink.CHAT_LINK)
            .where(ChatLink.CHAT_LINK.CHAT_ID.eq(chatId))
            .fetchInto(DTOSub.class);
    }

    @Transactional
    @Override
    public List<DTOSub> findByLinkId(long linkId) {
        return context.selectFrom(ChatLink.CHAT_LINK)
            .where(ChatLink.CHAT_LINK.LINK_ID.eq(linkId))
            .fetchInto(DTOSub.class);
    }
}
