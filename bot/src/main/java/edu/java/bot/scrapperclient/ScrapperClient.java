package edu.java.bot.scrapperclient;

import edu.java.models.Request.AddLinkRequest;
import edu.java.models.Request.RemoveLinkRequest;
import edu.java.models.Response.LinkResponse;
import edu.java.models.Response.ListLinksResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@SuppressWarnings("MultipleStringLiterals")
public class ScrapperClient {
    @Autowired
    @Qualifier("ScrapperClient")
    WebClient webClient;

    public void chatReg(long chatId) {
        webClient.post()
            .uri("/tg-chat/{id}", chatId)
            .retrieve()
            .onStatus(
                HttpStatusCode::is4xxClientError,
                error -> Mono.error(new RuntimeException("Chat id is not found")))
            .onStatus(HttpStatusCode::is5xxServerError,
                error -> Mono.error(new RuntimeException("Server is not responding")))
            .bodyToMono(String.class)
            .block();
    }

    public void chatDel(long chatId) {
        webClient.delete()
            .uri("/tg-chat/{id}", chatId)
            .retrieve()
            .onStatus(
                HttpStatusCode::is4xxClientError,
                error -> Mono.error(new RuntimeException("Chat id is not found")))
            .onStatus(HttpStatusCode::is5xxServerError,
                error -> Mono.error(new RuntimeException("Server is not responding")))
            .bodyToMono(String.class)
            .block();
    }

    public ListLinksResponse getLinks(Long chatId) {
        return webClient.get()
            .uri("/links")
            .header("Tg-Chat-Id", chatId.toString())
            .retrieve()
            .onStatus(
                HttpStatusCode::is4xxClientError,
                error -> Mono.error(new RuntimeException("Chat id is not found")))
            .onStatus(HttpStatusCode::is5xxServerError,
                error -> Mono.error(new RuntimeException("Server is not responding")))
            .bodyToMono(ListLinksResponse.class)
            .block();
    }

    public LinkResponse addLink(Long chatId, String link) {
        return webClient.post()
            .uri("/links")
            .header("Tg-Chat-Id", chatId.toString())
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(new AddLinkRequest(link)))
            .retrieve()
            .onStatus(
                HttpStatusCode::is4xxClientError,
                error -> Mono.error(new RuntimeException("Link is not found")))
            .onStatus(HttpStatusCode::is5xxServerError,
                error -> Mono.error(new RuntimeException("Server is not responding")))
            .bodyToMono(LinkResponse.class)
            .block();
    }

    public LinkResponse delLinks(Long chatId, String link) {
        return webClient.method(HttpMethod.DELETE)
            .uri("/links")
            .header("Tg-Chat-Id", chatId.toString())
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(new RemoveLinkRequest(link)))
            .retrieve()
            .onStatus(
                HttpStatusCode::is4xxClientError,
                error -> Mono.error(new RuntimeException("Link is not found")))
            .onStatus(HttpStatusCode::is5xxServerError,
                error -> Mono.error(new RuntimeException("Server is not responding")))
            .bodyToMono(LinkResponse.class)
            .block();
    }
}
