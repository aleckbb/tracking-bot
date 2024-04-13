package edu.java.scrapper.botclient;

import edu.java.models.Request.LinkUpdate;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class BotClient {

    private final WebClient webClient;

    public BotClient(WebClient.Builder builder, String url) {
        this.webClient = builder.baseUrl(url).build();
    }

    @Retryable(interceptor = "MyInterceptor")
    public void sendUpdate(long id, String url, String description, long[] tgChatIds) {
        webClient.post()
            .uri("/updates")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(new LinkUpdate(id, url, description, tgChatIds)))
            .retrieve()
            .onStatus(
                HttpStatusCode::is4xxClientError,
                error -> Mono.error(new RuntimeException("Incorrect query parameters"))
            )
            .onStatus(
                HttpStatusCode::is5xxServerError,
                error -> Mono.error(new RuntimeException("Server is not responding"))
            )
            .bodyToMono(Void.class)
            .block();
    }
}
