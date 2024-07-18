package edu.java.scrapper.linkUpdateService;

import edu.java.models.Request.LinkUpdate;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class BotClient implements LinkUpdateService {

    private final WebClient webClient;

    public BotClient(WebClient.Builder builder, String url) {
        this.webClient = builder.baseUrl(url).build();
    }

    @Override
    @Retryable(interceptor = "MyInterceptor")
    public void sendUpdate(LinkUpdate linkUpdate) {
        webClient.post()
            .uri("/updates")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(linkUpdate))
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
