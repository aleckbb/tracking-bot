package edu.java.clients;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.ScrapperApplication;
import edu.java.dtoClasses.github.GitHub;
import edu.java.dtoClasses.github.Repository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ScrapperApplication.class)
@WireMockTest
class GitHubClientTest {

    @Autowired
    GitHubClient gitHubClient;

    @RegisterExtension
    static WireMockExtension wireMockExtension = WireMockExtension.newInstance()
        .options(wireMockConfig().dynamicPort().dynamicPort())
        .build();

    @DynamicPropertySource
    public static void setUpMockBaseUrl(DynamicPropertyRegistry registry) {
        registry.add("app.base-url-github", wireMockExtension::baseUrl);
    }

    @Test
    @DisplayName("Клиент работает правильно")
    public void test1() {
        wireMockExtension.stubFor(
            WireMock.get("/repos/aleckbb/test")
                .willReturn(aResponse()
                    .withBody("{\"name\": \"mock\"}")
                    .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE))
        );
        wireMockExtension.stubFor(
            WireMock.get("/repos/aleckbb/test/branches")
                .willReturn(aResponse().withStatus(200))
        );
        wireMockExtension.stubFor(
            WireMock.get("/repos/aleckbb/test/pulls")
                .willReturn(aResponse().withStatus(200))
        );
        GitHub dtoGitHub = gitHubClient.getGitHub("aleckbb", "test");
        assertEquals("mock", dtoGitHub.repository().repoName());
    }

    @Test
    @DisplayName("Ошибка клиента")
    public void test2() {
        wireMockExtension.stubFor(
            WireMock.get("/repos/aleckbb/test")
                .willReturn(aResponse()
                    .withStatus(400))
        );
        String res = "";
        try {
            gitHubClient.getGitHub("aleckbb", "test");
        } catch (RuntimeException e) {
            res = e.getMessage();
        }

        assertEquals("API not found", res);
    }

    @Test
    @DisplayName("Ошибка сервера")
    public void test3() {
        wireMockExtension.stubFor(
            WireMock.get("/repos/aleckbb/test")
                .willReturn(aResponse()
                    .withStatus(500))
        );
        String res = "";
        try {
            gitHubClient.getGitHub("aleckbb", "test");
        } catch (RuntimeException e) {
            res = e.getMessage();
        }

        assertEquals("Server is not responding", res);
    }
}
