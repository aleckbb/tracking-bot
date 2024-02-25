package edu.java.clients;

import edu.java.dtoClasses.DTOGitHub;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
public class GitHubClient {
    @Autowired
    @Qualifier("GitHubClient")
    WebClient webClient;

    @GetMapping("/repos/{name}/{repo}")
    public DTOGitHub getDTOGitHub(@PathVariable String name, @PathVariable String repo) {
        return webClient.get()
            .uri("/repos/{name}/{repo}", name, repo)
            .retrieve()
            .onStatus(
                HttpStatusCode::is4xxClientError,
                error -> Mono.error(new RuntimeException("API not found"))
            )
            .onStatus(
                HttpStatusCode::is5xxServerError,
                error -> Mono.error(new RuntimeException("Server is not responding"))
            )
            .bodyToMono(DTOGitHub.class)
            .block();
    }
}
