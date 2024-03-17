package edu.java.service.jdbc;

import edu.java.dtoClasses.jdbc.DTOChat;
import edu.java.dtoClasses.jdbc.DTOLink;
import edu.java.dtoClasses.jdbc.DTOSub;
import edu.java.exceptions.AlreadyExistException;
import edu.java.exceptions.NotExistException;
import edu.java.repos.chat.ChatRepositoryImpl;
import edu.java.repos.chatLink.ChatLinkRepositoryImpl;
import edu.java.repos.link.LinkRepositoryImpl;
import edu.java.service.interfaces.LinkService;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JdbcLinkService implements LinkService {
    @Autowired
    private ChatRepositoryImpl chatRepository;
    @Autowired
    private LinkRepositoryImpl linkRepository;
    @Autowired
    private ChatLinkRepositoryImpl chatLinkRepository;

    @Override
    public void add(long chatId, String url, String username) throws AlreadyExistException {
        if (!isChatExists(chatId)) {
            chatRepository.add(new DTOChat(chatId, username, OffsetDateTime.now()));
        }

        DTOLink link = linkRepository.findByUrl(url);
        Long linkId;
        if (link == null) {
            OffsetDateTime time = OffsetDateTime.now();
            linkRepository.add(new DTOLink(null, url, time, time));
            linkId = linkRepository.findByUrl(url).linkId();
        } else {
            linkId = link.linkId();
        }
        List<DTOSub> subs = chatLinkRepository.findByChatId(chatId);
        if (pairIsExists(subs, linkId)) {
            throw new AlreadyExistException("Такая ссылка уже отслеживается");
        }
        chatLinkRepository.add(new DTOSub(chatId, linkId));
    }

    @Override
    public void remove(long chatId, String url) throws NotExistException {
        if (linkRepository.findByUrl(url) == null) {
            throw new NotExistException("Такой ссылки не отслеживается");
        }
        List<DTOSub> links = chatLinkRepository.findByChatId(chatId);
        for (DTOSub link : links) {
            List<DTOSub> subs = chatLinkRepository.findByLinkId(link.linkId());
            if (linkRepository.findByUrl(url).linkId().equals(link.linkId())) {
                chatLinkRepository.remove(new DTOSub(chatId, link.linkId()));
                if (subs.size() == 1) {
                    linkRepository.remove(new DTOLink(link.linkId(), null, null, null));
                }
            }
        }
    }

    @Override
    public List<DTOLink> listAll(long chatId) {
        return chatLinkRepository.findByChatId(chatId)
            .stream()
            .map(DTOSub::linkId)
            .map(linkId -> {
                List<DTOLink> links = linkRepository.findAll();
                for (DTOLink link : links) {
                    if (linkId.equals(link.linkId())) {
                        return link;
                    }
                }
                return null;
            })
            .filter(Objects::nonNull)
            .toList();
    }

    private boolean isChatExists(long id) {
        long chatCount = chatRepository.findAll()
            .stream()
            .filter(c -> c.chatId() == id)
            .count();

        return chatCount == 1;
    }

    private boolean pairIsExists(List<DTOSub> subs, long linkId) {
        for (DTOSub sub : subs) {
            if (sub.linkId() == linkId) {
                return true;
            }
        }
        return false;
    }
}
