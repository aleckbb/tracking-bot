package edu.java.service.jdbc;

import edu.java.dtoClasses.jdbc.DTOChat;
import edu.java.dtoClasses.jdbc.DTOLink;
import edu.java.dtoClasses.jdbc.DTOSub;
import edu.java.exceptions.NotExistException;
import edu.java.exceptions.RepeatedRegistrationException;
import edu.java.repos.jdbc.ChatLinkRepositoryImpl;
import edu.java.repos.jdbc.ChatRepositoryImpl;
import edu.java.repos.jdbc.LinkRepositoryImpl;
import edu.java.service.interfaces.ChatService;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JdbcChatService implements ChatService {
    @Autowired
    private ChatRepositoryImpl chatRepository;
    @Autowired
    private LinkRepositoryImpl linkRepository;
    @Autowired
    private ChatLinkRepositoryImpl chatLinkRepository;

    @Override
    public void register(long chatId, String username) throws RepeatedRegistrationException {
        if (isChatExists(chatId)) {
            throw new RepeatedRegistrationException("Чат уже существует!");
        }
        chatRepository.add(new DTOChat(chatId, username, OffsetDateTime.now()));
    }

    @Override
    public void unregister(long chatId) throws NotExistException {
        if (!isChatExists(chatId)) {
            throw new NotExistException("Чата не существует");
        }
        List<DTOSub> links = chatLinkRepository.findByChatId(chatId);
        for (DTOSub link : links) {
            List<DTOSub> subs = chatLinkRepository.findByLinkId(link.linkId());
            chatLinkRepository.remove(new DTOSub(chatId, link.linkId()));
            if (subs.size() == 1) {
                linkRepository.remove(new DTOLink(link.linkId(), null, null, null, null, null));
            }
        }
        chatRepository.remove(new DTOChat(chatId, null, null));
    }

    private boolean isChatExists(long id) {
        long chatCount = chatRepository.findAll()
            .stream()
            .filter(c -> c.chatId() == id)
            .count();

        return chatCount == 1;
    }
}
