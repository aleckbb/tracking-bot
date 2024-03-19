package edu.java.clients;

import edu.java.dtoClasses.github.DTOGitHub;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class GitHubClient {
    private final WebClient webClient;

    public GitHubClient(WebClient.Builder builder, String url) {
        this.webClient = builder.baseUrl(url).build();
    }

    public DTOGitHub getDTOGitHub(String name, String repo) {
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
