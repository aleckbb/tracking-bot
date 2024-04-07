package edu.java.clients;

import edu.java.dtoClasses.github.Branch;
import edu.java.dtoClasses.github.GitHub;
import edu.java.dtoClasses.github.PullRequest;
import edu.java.dtoClasses.github.Repository;
import org.springframework.http.HttpStatusCode;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@SuppressWarnings("MultipleStringLiterals")
public class GitHubClient {
    private final WebClient webClient;

    public GitHubClient(WebClient.Builder builder, String url) {
        this.webClient = builder.baseUrl(url).build();
    }

    @Retryable(interceptor = "MyInterceptor")
    public GitHub getGitHub(String name, String repo) {
        Repository repository = webClient.get()
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
            .bodyToMono(Repository.class)
            .block();
        Branch[] branches = webClient.get()
            .uri("/repos/{name}/{repo}/branches", name, repo)
            .retrieve()
            .onStatus(
                HttpStatusCode::is4xxClientError,
                error -> Mono.error(new RuntimeException("API not found"))
            )
            .onStatus(
                HttpStatusCode::is5xxServerError,
                error -> Mono.error(new RuntimeException("Server is not responding"))
            )
            .bodyToMono(Branch[].class)
            .block();
        PullRequest[] pullRequests = webClient.get()
            .uri("/repos/{name}/{repo}/pulls", name, repo)
            .retrieve()
            .onStatus(
                HttpStatusCode::is4xxClientError,
                error -> Mono.error(new RuntimeException("API not found"))
            )
            .onStatus(
                HttpStatusCode::is5xxServerError,
                error -> Mono.error(new RuntimeException("Server is not responding"))
            )
            .bodyToMono(PullRequest[].class)
            .block();
        return new GitHub(branches, pullRequests, repository);
    }
}
