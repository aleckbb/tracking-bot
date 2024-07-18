package edu.java.scrapper.service.jooq;

import edu.java.scrapper.dtoClasses.github.GitHub;
import edu.java.scrapper.dtoClasses.jdbc.DTOChat;
import edu.java.scrapper.dtoClasses.jdbc.DTOLink;
import edu.java.scrapper.dtoClasses.jdbc.DTOSub;
import edu.java.scrapper.dtoClasses.sof.StackOverflow;
import edu.java.scrapper.exceptions.AlreadyExistException;
import edu.java.scrapper.exceptions.NotExistException;
import edu.java.scrapper.repos.jooq.JooqChatLinkRepository;
import edu.java.scrapper.repos.jooq.JooqChatRepository;
import edu.java.scrapper.repos.jooq.JooqLinkRepository;
import edu.java.scrapper.service.handlers.GitHubHandler;
import edu.java.scrapper.service.handlers.SofHandler;
import edu.java.scrapper.service.interfaces.LinkService;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@SuppressWarnings("MultipleStringLiterals")
public class JooqLinkService implements LinkService {
    private final JooqChatRepository jooqChatRepository;
    private final JooqChatLinkRepository jooqChatLinkRepository;
    private final JooqLinkRepository jooqLinkRepository;
    private final GitHubHandler gitHubHandler;
    private final SofHandler sofHandler;

    @Override
    public void add(long chatId, String url, String username) throws AlreadyExistException {
        if (!jooqChatRepository.existsById(chatId)) {
            jooqChatRepository.add(new DTOChat(chatId, username, OffsetDateTime.now()));
        }

        DTOLink link = jooqLinkRepository.findByUrl(url);
        Long linkId;
        if (link == null) {
            String type = getType(url);
            OffsetDateTime checkedAt = OffsetDateTime.now();
            Object[] dataAndTime = getData(type, url);
            String data = (String) dataAndTime[0];
            OffsetDateTime updateAt = (OffsetDateTime) dataAndTime[1];
            jooqLinkRepository.add(new DTOLink(null, url, updateAt, checkedAt, type, data));
            linkId = jooqLinkRepository.findByUrl(url).getLinkId();
        } else {
            linkId = link.getLinkId();
        }
        List<DTOSub> subs = jooqChatLinkRepository.findByChatId(chatId);
        if (pairIsExists(subs, linkId)) {
            throw new AlreadyExistException("Такая ссылка уже отслеживается");
        }
        jooqChatLinkRepository.add(new DTOSub(chatId, linkId));
    }

    @Override
    public void remove(long chatId, String url) throws NotExistException {
        if (jooqLinkRepository.findByUrl(url) == null) {
            throw new NotExistException("Такой ссылки не отслеживается");
        }
        List<DTOSub> links = jooqChatLinkRepository.findByChatId(chatId);
        boolean isLinkExist = false;
        for (DTOSub link : links) {
            List<DTOSub> subs = jooqChatLinkRepository.findByLinkId(link.linkId());
            if (jooqLinkRepository.findByUrl(url).getLinkId().equals(link.linkId())) {
                isLinkExist = true;
                jooqChatLinkRepository.remove(new DTOSub(chatId, link.linkId()));
                if (subs.size() == 1) {
                    jooqLinkRepository.remove(new DTOLink(link.linkId(), null, null, null, null, null));
                }
            }
            if (isLinkExist) {
                break;
            }
        }
        if (!isLinkExist) {
            throw new NotExistException("Такой ссылки не отслеживается");
        }
    }

    @Override
    public List<DTOLink> listAll(long chatId) {
        return jooqChatLinkRepository.findByChatId(chatId)
            .stream()
            .map(DTOSub::linkId)
            .map(linkId -> {
                List<DTOLink> links = jooqLinkRepository.findAll();
                for (DTOLink link : links) {
                    if (linkId.equals(link.getLinkId())) {
                        return link;
                    }
                }
                return null;
            })
            .filter(Objects::nonNull)
            .toList();
    }

    public Object[] getData(String type, String url) {
        OffsetDateTime updateAt;
        switch (type) {
            case "github" -> {
                GitHub gitHub = gitHubHandler.getInfo(url);
                updateAt = gitHub.repository().pushedTime();
                return new Object[] {gitHubHandler.getData(gitHub), updateAt};
            }
            case "stackoverflow" -> {
                StackOverflow stackOverflow = sofHandler.getInfo(url);
                updateAt = stackOverflow.items().getFirst().lastActivityDate();
                return new Object[] {sofHandler.getData(stackOverflow), updateAt};
            }
            default -> {
                return new Object[] {};
            }
        }
    }

    private boolean pairIsExists(List<DTOSub> subs, long linkId) {
        for (DTOSub sub : subs) {
            if (sub.linkId() == linkId) {
                return true;
            }
        }
        return false;
    }

    private String getType(String url) {
        if (url.contains("github.com")) {
            return "github";
        } else if (url.contains("stackoverflow.com")) {
            return "stackoverflow";
        }
        return "";
    }
}
