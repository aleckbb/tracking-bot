package edu.java.scrapper.service.jpa;

import edu.java.scrapper.exceptions.NotExistException;
import edu.java.scrapper.exceptions.RepeatedRegistrationException;
import edu.java.scrapper.repos.jpa.JpaChatRepository;
import edu.java.scrapper.repos.jpa.JpaLinkRepository;
import edu.java.scrapper.repos.jpa.entities.Chat;
import edu.java.scrapper.repos.jpa.entities.Link;
import edu.java.scrapper.service.interfaces.ChatService;
import java.time.OffsetDateTime;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
public class JpaChatService implements ChatService {
    private final JpaChatRepository jpaChatRepository;
    private final JpaLinkRepository jpaLinkRepository;

    @Override
    public void register(long chatId, String username) throws RepeatedRegistrationException {
        if (jpaChatRepository.existsById(chatId)) {
            throw new RepeatedRegistrationException("Чат уже существует!");
        }
        jpaChatRepository.saveAndFlush(new Chat(chatId, OffsetDateTime.now(), username));
    }

    @Override
    public void unregister(long chatId) throws NotExistException {
        if (!jpaChatRepository.existsById(chatId)) {
            throw new NotExistException("Чата не существует");
        }
        Set<Link> links = jpaChatRepository.findById(chatId).orElseThrow().getLinks();
        jpaChatRepository.deleteById(chatId);
        for (Link link : links) {
            if (link.getChats().size() == 1) {
                jpaLinkRepository.deleteById(link.getLinkId());
            }
        }
        jpaLinkRepository.flush();
        jpaChatRepository.flush();
    }

    @Override
    public Boolean userExist(long chatId) {
        return jpaChatRepository.existsById(chatId);
    }
}
