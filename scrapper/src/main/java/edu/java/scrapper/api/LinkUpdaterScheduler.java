package edu.java.scrapper.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.java.scrapper.botclient.BotClient;
import edu.java.scrapper.dtoClasses.github.GitHub;
import edu.java.scrapper.dtoClasses.jdbc.DTOLink;
import edu.java.scrapper.dtoClasses.sof.StackOverflow;
import edu.java.scrapper.repos.data.GitHubData;
import edu.java.scrapper.repos.data.SofData;
import edu.java.scrapper.service.handlers.GitHubHandler;
import edu.java.scrapper.service.handlers.SofHandler;
import edu.java.scrapper.service.interfaces.LinkUpdater;
import io.swagger.v3.core.util.Json;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@SuppressWarnings({"MultipleStringLiterals", "MagicNumber"})
@Component
@EnableScheduling
public class LinkUpdaterScheduler {
    @Autowired
    private LinkUpdater linkUpdater;
    @Autowired
    private BotClient botClient;
    @Autowired
    private SofHandler sofHandler;
    @Autowired
    private GitHubHandler gitHubHandler;

    @Scheduled(fixedDelayString = "#{scheduler.interval}")
    public void update() {
        OffsetDateTime time = OffsetDateTime.now();
        List<DTOLink> oldLinks = linkUpdater.findOldLinksToUpdate(time);
        for (DTOLink link : oldLinks) {
            time = OffsetDateTime.now();
            linkUpdater.check(link.getLinkId(), time);
            String description;
            switch (link.getLinkType()) {
                case "github" -> {
                    description = gitHubUpdate(link);
                }
                case "stackoverflow" -> {
                    description = sofUpdate(link);
                }
                default -> {
                    description = "";
                }
            }
            if (!description.isEmpty()) {
                botClient.sendUpdate(
                    link.getLinkId(),
                    link.getUrl(),
                    description,
                    linkUpdater.allChatIdsByLinkId(link.getLinkId())
                );
            }
        }
    }

    private String sofUpdate(DTOLink link) {
        StringBuilder description = new StringBuilder();
        StackOverflow stackOverflow = sofHandler.getInfo(link.getUrl());
        try {
            SofData sofData = Json.mapper().readValue(link.getData(), SofData.class);
            StackOverflow.Question question = stackOverflow.items().getFirst();
            if (question.lastActivityDate().plusHours(3).isAfter(link.getUpdateAt())
                || (!sofData.isAnswered() && question.isAnswered())) {
                linkUpdater.update(link.getLinkId(), question.lastActivityDate(), sofHandler.getData(stackOverflow));
                description.append("В вопросе \"").append(question.title()).append("\" по ссылке ")
                    .append(link.getUrl());
                if (!sofData.isAnswered() && question.isAnswered()) {
                    description.append(" автор отметил один из ответов правильным. ");
                } else {
                    description.append(" был добавлен ответ. ");
                }
                description.append("Скорее переходите по ссылке!");
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return description.toString();
    }

    private String gitHubUpdate(DTOLink link) {
        StringBuilder description = new StringBuilder();
        GitHub gitHub = gitHubHandler.getInfo(link.getUrl());
        try {
            GitHubData gitHubData = Json.mapper().readValue(link.getData(), GitHubData.class);
            if (gitHub.repository().pushedTime().plusHours(3).isAfter(link.getUpdateAt())) {
                linkUpdater.update(link.getLinkId(), gitHub.repository().pushedTime(), gitHubHandler.getData(gitHub));
                description.append("В репозитории ").append(gitHub.repository().repoName()).append(" по ссылке ")
                    .append(link.getUrl());
                String begDescription = description.toString();
                if (gitHubData.numberOfBranches() < gitHub.branches().length) {
                    description.append(" была добавлена ветка ").append(". ");
                } else if (gitHubData.numberOfBranches() > gitHub.branches().length) {
                    description.append(" была удалена ветка. ");
                } else if (Arrays.toString(gitHub.branches()).hashCode() != gitHubData.branchesHash()) {
                    description.append(" был добавлен новый коммит. ");
                }
                if (gitHubData.numberOfPullRequests() < gitHub.pullRequests().length) {
                    description.append(" был открыт пулл реквест №").append(gitHub.pullRequests()[0].number())
                        .append(" c заголовком ").append(gitHub.pullRequests()[0].title()).append(". ");
                } else if (gitHubData.numberOfPullRequests() > gitHub.pullRequests().length) {
                    description.append(" был закрыт пулл реквест. ");
                } else if (Arrays.toString(gitHub.pullRequests()).hashCode() != gitHubData.pullRequestsHash()) {
                    description.append(" был добавлен новый коммит. ");
                }
                if (description.toString().equals(begDescription)) {
                    description.append(" есть обновление. ");
                }
                description.append("Скорее переходите по ссылке!");
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return description.toString();
    }
}
