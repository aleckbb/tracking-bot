package edu.java.scrapper.service.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.java.scrapper.clients.GitHubClient;
import edu.java.scrapper.dtoClasses.github.GitHub;
import edu.java.scrapper.repos.data.GitHubData;
import io.swagger.v3.core.util.Json;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GitHubHandler implements Handler<GitHub> {
    @Autowired
    private GitHubClient gitHubClient;

    @Override
    public String getData(GitHub dto) {
        try {
            return Json.mapper().writeValueAsString(
                new GitHubData(
                    dto.branches().length,
                    dto.pullRequests().length,
                    Arrays.toString(dto.branches()).hashCode(),
                    Arrays.toString(dto.pullRequests()).hashCode()
                )
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public GitHub getInfo(String url) {
        String[] splitUrl = url.split("/");
        return gitHubClient.getGitHub(splitUrl[splitUrl.length - 2], splitUrl[splitUrl.length - 1]);
    }
}
