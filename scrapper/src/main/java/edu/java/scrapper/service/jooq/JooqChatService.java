package edu.java.scrapper.service.jooq;

import edu.java.scrapper.dtoClasses.jdbc.DTOChat;
import edu.java.scrapper.dtoClasses.jdbc.DTOLink;
import edu.java.scrapper.dtoClasses.jdbc.DTOSub;
import edu.java.scrapper.exceptions.NotExistException;
import edu.java.scrapper.exceptions.RepeatedRegistrationException;
import edu.java.scrapper.repos.jooq.JooqChatLinkRepository;
import edu.java.scrapper.repos.jooq.JooqChatRepository;
import edu.java.scrapper.repos.jooq.JooqLinkRepository;
import edu.java.scrapper.service.interfaces.ChatService;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JooqChatService implements ChatService {
    private final JooqChatRepository jooqChatRepository;
    private final JooqChatLinkRepository jooqChatLinkRepository;
    private final JooqLinkRepository jooqLinkRepository;

    @Override
    public void register(long chatId, String username) throws RepeatedRegistrationException {
        if (jooqChatRepository.existsById(chatId)) {
            throw new RepeatedRegistrationException("Чат уже существует!");
        }
        jooqChatRepository.add(new DTOChat(chatId, username, OffsetDateTime.now()));
    }

    @Override
    public void unregister(long chatId) throws NotExistException {
        if (!jooqChatRepository.existsById(chatId)) {
            throw new NotExistException("Чата не существует");
        }
        List<DTOSub> links = jooqChatLinkRepository.findByChatId(chatId);
        for (DTOSub link : links) {
            List<DTOSub> subs = jooqChatLinkRepository.findByLinkId(link.linkId());
            jooqChatLinkRepository.remove(new DTOSub(chatId, link.linkId()));
            if (subs.size() == 1) {
                jooqLinkRepository.remove(new DTOLink(link.linkId(), null, null, null, null, null));
            }
        }
        jooqChatRepository.remove(new DTOChat(chatId, null, null));
    }

    @Override
    public Boolean userExist(long chatId) {
        return jooqChatRepository.existsById(chatId);
    }
}
