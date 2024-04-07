package edu.java.clients;

import edu.java.dtoClasses.sof.StackOverflow;
import org.springframework.http.HttpStatusCode;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class StackOverflowClient {
    private final WebClient webClient;

    public StackOverflowClient(WebClient.Builder builder, String url) {
        this.webClient = builder.baseUrl(url).build();
    }

    @Retryable(interceptor = "MyInterceptor")
    public StackOverflow getStackOverflow(String questionId) {
        return webClient.get()
            .uri("/questions/{ids}?site=stackoverflow", questionId)
            .retrieve()
            .onStatus(
                HttpStatusCode::is4xxClientError,
                error -> Mono.error(new RuntimeException("API not found"))
            )
            .onStatus(
                HttpStatusCode::is5xxServerError,
                error -> Mono.error(new RuntimeException("Server is not responding"))
            )
            .bodyToMono(StackOverflow.class)
            .block();
    }
}
