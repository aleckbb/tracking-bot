package edu.java.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.java.botclient.BotClient;
import edu.java.dtoClasses.github.GitHub;
import edu.java.dtoClasses.jdbc.DTOLink;
import edu.java.dtoClasses.sof.StackOverflow;
import edu.java.repos.data.GitHubData;
import edu.java.repos.data.SofData;
import edu.java.service.handlers.GitHubHandler;
import edu.java.service.handlers.SofHandler;
import edu.java.service.interfaces.LinkUpdater;
import io.swagger.v3.core.util.Json;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@SuppressWarnings("MultipleStringLiterals")
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
            linkUpdater.check(link.linkId(), time);
            String description;
            switch (link.linkType()) {
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
                    link.linkId(),
                    link.url(),
                    description,
                    linkUpdater.allChatIdsByLinkId(link.linkId())
                );
            }
        }
    }

    private String sofUpdate(DTOLink link) {
        StringBuilder description = new StringBuilder();
        StackOverflow stackOverflow = sofHandler.getInfo(link.url());
        try {
            SofData sofData = Json.mapper().readValue(link.data(), SofData.class);
            StackOverflow.Question question = stackOverflow.items().getFirst();
            if (question.lastActivityDate().isAfter(link.updateAt())
                || (!sofData.isAnswered() && question.isAnswered())) {
                linkUpdater.update(link.linkId(), question.lastActivityDate(), sofHandler.getData(stackOverflow));
                description.append("В вопросе \"").append(question.title()).append("\" по ссылке ")
                    .append(link.url());
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
        GitHub gitHub = gitHubHandler.getInfo(link.url());
        try {
            GitHubData gitHubData = Json.mapper().readValue(link.data(), GitHubData.class);
            if (gitHub.repository().pushedTime().isAfter(link.updateAt())) {
                linkUpdater.update(link.linkId(), gitHub.repository().pushedTime(), gitHubHandler.getData(gitHub));
                description.append("В репозитории ").append(gitHub.repository().repoName()).append(" по ссылке ")
                    .append(link.url());
                String begDescription = description.toString();
                if (gitHubData.numberOfBranches() < gitHub.branches().length) {
                    description.append(" была добавлена ветка ").append(gitHub.branches()[0].name()).append(". ");
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
