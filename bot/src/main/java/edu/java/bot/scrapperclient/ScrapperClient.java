package edu.java.bot.scrapperclient;

import edu.java.bot.scrapperclient.Exceptions.BadRequestException;
import edu.java.bot.scrapperclient.Exceptions.ServerErrorException;
import edu.java.bot.scrapperclient.Exceptions.TooManyRequestException;
import edu.java.models.Request.AddLinkRequest;
import edu.java.models.Request.RemoveLinkRequest;
import edu.java.models.Response.LinkResponse;
import edu.java.models.Response.ListLinksResponse;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@SuppressWarnings("MultipleStringLiterals")
public class ScrapperClient {
    private final WebClient webClient;

    public ScrapperClient(WebClient.Builder builder, String url) {
        this.webClient = builder.baseUrl(url).build();
    }

    @Retryable(interceptor = "MyInterceptor")
    public void chatReg(long chatId, String username) {
        webClient.post()
            .uri("/tg-chat/{id}", chatId)
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(username))
            .retrieve()
            .onStatus(
                HttpStatus.TOO_MANY_REQUESTS::equals,
                error -> Mono.error(new TooManyRequestException("Too many requests"))
            )
            .onStatus(
                HttpStatusCode::is4xxClientError,
                error -> Mono.error(new BadRequestException("Chat id is not found"))
            )
            .onStatus(
                HttpStatusCode::is5xxServerError,
                error -> Mono.error(new ServerErrorException("Server is not responding"))
            )
            .bodyToMono(Void.class)
            .block();
    }

    @Retryable(interceptor = "MyInterceptor")
    public void chatDel(long chatId) {
        webClient.delete()
            .uri("/tg-chat/{id}", chatId)
            .retrieve()
            .onStatus(
                HttpStatus.TOO_MANY_REQUESTS::equals,
                error -> Mono.error(new TooManyRequestException("Too many requests"))
            )
            .onStatus(
                HttpStatusCode::is4xxClientError,
                error -> Mono.error(new BadRequestException("Chat id is not found"))
            )
            .onStatus(
                HttpStatusCode::is5xxServerError,
                error -> Mono.error(new ServerErrorException("Server is not responding"))
            )
            .bodyToMono(Void.class)
            .block();
    }

    @Retryable(interceptor = "MyInterceptor")
    public ListLinksResponse getLinks(Long chatId) {
        return webClient.get()
            .uri("/links")
            .header("Tg-Chat-Id", chatId.toString())
            .retrieve()
            .onStatus(
                HttpStatus.TOO_MANY_REQUESTS::equals,
                error -> Mono.error(new TooManyRequestException("Too many requests"))
            )
            .onStatus(
                HttpStatusCode::is4xxClientError,
                error -> Mono.error(new BadRequestException("Chat id is not found"))
            )
            .onStatus(
                HttpStatusCode::is5xxServerError,
                error -> Mono.error(new ServerErrorException("Server is not responding"))
            )
            .bodyToMono(ListLinksResponse.class)
            .block();
    }

    @Retryable(interceptor = "MyInterceptor")
    public LinkResponse addLink(Long chatId, String username, String link) {
        return webClient.post()
            .uri("/links")
            .header("Tg-Chat-Id", chatId.toString())
            .header("Username", username)
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(new AddLinkRequest(link)))
            .retrieve()
            .onStatus(
                HttpStatus.TOO_MANY_REQUESTS::equals,
                error -> Mono.error(new TooManyRequestException("Too many requests"))
            )
            .onStatus(
                HttpStatusCode::is4xxClientError,
                error -> Mono.error(new BadRequestException("Link is not found"))
            )
            .onStatus(
                HttpStatusCode::is5xxServerError,
                error -> Mono.error(new ServerErrorException("Server is not responding"))
            )
            .bodyToMono(LinkResponse.class)
            .block();
    }

    @Retryable(interceptor = "MyInterceptor")
    public LinkResponse delLinks(Long chatId, String link) {
        return webClient.method(HttpMethod.DELETE)
            .uri("/links")
            .header("Tg-Chat-Id", chatId.toString())
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(new RemoveLinkRequest(link)))
            .retrieve()
            .onStatus(
                HttpStatus.TOO_MANY_REQUESTS::equals,
                error -> Mono.error(new TooManyRequestException("Too many requests"))
            )
            .onStatus(
                HttpStatusCode::is4xxClientError,
                error -> Mono.error(new BadRequestException("Link is not found"))
            )
            .onStatus(
                HttpStatusCode::is5xxServerError,
                error -> Mono.error(new ServerErrorException("Server is not responding"))
            )
            .bodyToMono(LinkResponse.class)
            .block();
    }

    @Retryable(interceptor = "MyInterceptor")
    public Boolean hasUser(Long chatId) {
        return webClient.get()
            .uri("/tg-chat/{id}", chatId)
            .retrieve()
            .onStatus(
                HttpStatus.TOO_MANY_REQUESTS::equals,
                error -> Mono.error(new TooManyRequestException("Too many requests"))
            )
            .onStatus(
                HttpStatusCode::is4xxClientError,
                error -> Mono.error(new BadRequestException("Chat id is not found"))
            )
            .onStatus(
                HttpStatusCode::is5xxServerError,
                error -> Mono.error(new ServerErrorException("Server is not responding"))
            )
            .bodyToMono(Boolean.class)
            .block();
    }
}
