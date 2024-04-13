package edu.java.scrapper.clients;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.scrapper.ScrapperApplication;
import edu.java.scrapper.dtoClasses.sof.StackOverflow;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ScrapperApplication.class)
@WireMockTest
@DirtiesContext
class StackOverFlowClientTest {
    @Autowired
    StackOverflowClient stackOverFlowClient;

    @RegisterExtension
    static WireMockExtension wireMockExtension = WireMockExtension.newInstance()
        .options(wireMockConfig().dynamicPort().dynamicPort())
        .build();

    @DynamicPropertySource
    public static void setUpMockBaseUrl(DynamicPropertyRegistry registry) {
        registry.add("app.base-url-stackoverflow", wireMockExtension::baseUrl);
    }

    @Test
    @DisplayName("Клиент работает правильно")
    public void test1() {
        wireMockExtension.stubFor(
            WireMock.get("/questions/78056316?site=stackoverflow")
                .willReturn(aResponse()
                    .withBody("{\"items\":[{\"title\":11111111}]}")
                    .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE))
        );
        StackOverflow dtoStackOverflow = stackOverFlowClient.getStackOverflow("78056316");
        assertEquals("11111111", dtoStackOverflow.items().getFirst().title());
    }

    @Test
    @DisplayName("Ошибка клиента")
    public void test2() {
        wireMockExtension.stubFor(
            WireMock.get("/questions/78056316?site=stackoverflow")
                .willReturn(aResponse()
                    .withStatus(400))
        );
        String res = "";
        try {
            stackOverFlowClient.getStackOverflow("78056316");
        } catch (RuntimeException e) {
            res = e.getMessage();
        }

        assertEquals("API not found", res);
    }

    @Test
    @DisplayName("Ошибка сервера")
    public void test3() {
        wireMockExtension.stubFor(
            WireMock.get("/questions/78056316?site=stackoverflow")
                .willReturn(aResponse()
                    .withStatus(500))
        );
        String res = "";
        try {
            stackOverFlowClient.getStackOverflow("78056316");
        } catch (RuntimeException e) {
            res = e.getMessage();
        }

        assertEquals("Server is not responding", res);
    }
}
