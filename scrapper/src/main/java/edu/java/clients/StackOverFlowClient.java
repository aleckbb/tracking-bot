package edu.java.clients;

import edu.java.dtoClasses.DTOStackOverflow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class StackOverFlowClient {
    private final WebClient webClient;

    public StackOverFlowClient(WebClient.Builder builder, String url) {
        this.webClient = builder.baseUrl(url).build();
    }

    public DTOStackOverflow getDTOStackOverflow(String ids) {
        return webClient.get()
            .uri("/questions/{ids}?site=stackoverflow", ids)
            .retrieve()
            .onStatus(
                HttpStatusCode::is4xxClientError,
                error -> Mono.error(new RuntimeException("API not found"))
            )
            .onStatus(
                HttpStatusCode::is5xxServerError,
                error -> Mono.error(new RuntimeException("Server is not responding"))
            )
            .bodyToMono(DTOStackOverflow.class)
            .block();
    }
}
