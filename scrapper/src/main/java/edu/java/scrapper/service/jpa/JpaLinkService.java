package edu.java.scrapper.service.jpa;

import edu.java.scrapper.dtoClasses.github.GitHub;
import edu.java.scrapper.dtoClasses.jdbc.DTOLink;
import edu.java.scrapper.dtoClasses.sof.StackOverflow;
import edu.java.scrapper.exceptions.AlreadyExistException;
import edu.java.scrapper.exceptions.NotExistException;
import edu.java.scrapper.repos.jpa.JpaChatRepository;
import edu.java.scrapper.repos.jpa.JpaLinkRepository;
import edu.java.scrapper.repos.jpa.entities.Chat;
import edu.java.scrapper.repos.jpa.entities.Link;
import edu.java.scrapper.service.handlers.GitHubHandler;
import edu.java.scrapper.service.handlers.SofHandler;
import edu.java.scrapper.service.interfaces.LinkService;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings("MultipleStringLiterals")
@RequiredArgsConstructor
@Transactional
public class JpaLinkService implements LinkService {
    private final JpaLinkRepository jpaLinkRepository;
    private final JpaChatRepository jpaChatRepository;
    private final GitHubHandler gitHubHandler;
    private final SofHandler sofHandler;

    @Override
    public void add(long chatId, String url, String username) throws AlreadyExistException {
        Chat chat;
        if (!jpaChatRepository.existsById(chatId)) {
            chat = new Chat(chatId, OffsetDateTime.now(), username);
            jpaChatRepository.saveAndFlush(chat);
        } else {
            chat = jpaChatRepository.findById(chatId).orElseThrow();
        }
        if (!jpaLinkRepository.existsByUrl(url)) {
            String type = getType(url);
            OffsetDateTime checkedAt = OffsetDateTime.now();
            Object[] dataAndTime = getData(type, url);
            String data = (String) dataAndTime[0];
            OffsetDateTime updateAt = (OffsetDateTime) dataAndTime[1];
            Link link = new Link(url, updateAt, checkedAt, type, data, Set.of(chat));
            jpaLinkRepository.saveAndFlush(link);
        } else {
            if (chat.getLinks().contains(jpaLinkRepository.findByUrl(url))) {
                throw new AlreadyExistException("Такая ссылка уже отслеживается");
            }
        }
        chat.getLinks().add(jpaLinkRepository.findByUrl(url));
        jpaChatRepository.flush();
    }

    @Override
    public void remove(long chatId, String url) throws NotExistException {
        Chat chat = jpaChatRepository.findById(chatId).orElseThrow();
        if (!chat.getLinks().contains(jpaLinkRepository.findByUrl(url))) {
            throw new NotExistException("Такой ссылки не отслеживается");
        }
        chat.getLinks().remove(jpaLinkRepository.findByUrl(url));
        jpaChatRepository.flush();
        jpaLinkRepository.flush();
        if (jpaLinkRepository.findByUrl(url).getChats().size() == 1) {
            jpaLinkRepository.deleteById(jpaLinkRepository.findByUrl(url).getLinkId());
        }
        jpaLinkRepository.flush();
        jpaChatRepository.flush();
    }

    @Override
    public List<DTOLink> listAll(long chatId) {
        return jpaChatRepository.findById(chatId).orElseThrow().getLinks().stream().map(DTOLink::new).toList();
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

    private String getType(String url) {
        if (url.contains("github.com")) {
            return "github";
        } else if (url.contains("stackoverflow.com")) {
            return "stackoverflow";
        }
        return "";
    }
}
