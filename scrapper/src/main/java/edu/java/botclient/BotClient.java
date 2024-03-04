package edu.java.botclient;

import edu.java.models.Request.LinkUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class BotClient {

    @Autowired
    @Qualifier("BotClient")
    WebClient webClient;

    public void sendUpdate(int id, String url, String description, long[] tgChatIds) {
        webClient.post()
            .uri("/updates")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(new LinkUpdate(id, url, description, tgChatIds)))
            .retrieve()
            .onStatus(
                HttpStatusCode::is4xxClientError,
                error -> Mono.error(new RuntimeException("Incorrect query parameters")))
            .onStatus(HttpStatusCode::is5xxServerError,
                error -> Mono.error(new RuntimeException("Server is not responding")))
            .bodyToMono(String.class)
            .block();
    }
}
