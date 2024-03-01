import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.ScrapperApplication;
import edu.java.clients.StackOverFlowClient;
import edu.java.dtoClasses.DTOStackOverflow;
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
class StackOverFlowClientTest {
    @Autowired
    StackOverFlowClient stackOverFlowClient;

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
                    .withBody("{\"items\":[{\"question_id\":11111111}]}")
                    .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE))
        );
        DTOStackOverflow dtoStackOverflow = stackOverFlowClient.getDTOStackOverflow("78056316");
        assertEquals("11111111", dtoStackOverflow.items().getFirst().id());
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
            stackOverFlowClient.getDTOStackOverflow("78056316");
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
            stackOverFlowClient.getDTOStackOverflow("78056316");
        } catch (RuntimeException e) {
            res = e.getMessage();
        }

        assertEquals("Server is not responding", res);
    }
}
