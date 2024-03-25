import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.scrapperclient.ScrapperClient;
import edu.java.bot.service.Dialog;
import java.net.MalformedURLException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class Hw1Test {
    private static final WireMockServer server = new WireMockServer();

    @AfterAll
    static void tearDown() {
        server.stop();
    }

    ScrapperClient scrapperClient = new ScrapperClient(WebClient.builder(), "http://localhost:8080");

    @Test
    @DisplayName("Тест команды /start")
    void test1() {
        // given
        Update update = mock(Update.class);
        server.start();
        stubFor(post(urlEqualTo("/tg-chat/2")).willReturn(aResponse().withStatus(200)));

        // when
        when(update.message()).thenReturn(mock(Message.class));
        when(update.message().chat()).thenReturn(mock(Chat.class));
        when(update.message().chat().id()).thenReturn(2L);
        when(update.message().chat().username()).thenReturn("oposum");
        when(update.message().text()).thenReturn("/start");

        Dialog dialog = new Dialog(scrapperClient);
        SendMessage response = dialog.onUpdateReceived(update);
        String result = "Привет, oposum, пометил тебя в блокнотике!";

        // then
        assertEquals(result, response.getParameters().get("text").toString());
    }

    @Test
    @DisplayName("Тест команды /help")
    void test2() {
        // given
        Update update = mock(Update.class);

        // when
        when(update.message()).thenReturn(mock(Message.class));
        when(update.message().chat()).thenReturn(mock(Chat.class));
        when(update.message().chat().id()).thenReturn(1L);
        when(update.message().chat().username()).thenReturn("oposum");
        when(update.message().text()).thenReturn("/help");

        Dialog dialog = new Dialog(scrapperClient);
        SendMessage response = dialog.onUpdateReceived(update);
        String result = """
            /start -- зарегистрировать пользователя
            /help -- вывести окно с командами
            /track -- начать отслеживание ссылки
            /untrack -- прекратить отслеживание ссылки
            /list -- показать список отслеживаемых ссылок""";

        // then
        assertEquals(result, response.getParameters().get("text").toString());
    }

    @Test
    @DisplayName("Тест команды /track")
    void test3() {
        // given
        Update update = mock(Update.class);
        server.start();
        stubFor(post(urlEqualTo("/links")).willReturn(aResponse().withStatus(200)));

        // when
        when(update.message()).thenReturn(mock(Message.class));
        when(update.message().chat()).thenReturn(mock(Chat.class));
        when(update.message().chat().id()).thenReturn(2L);
        when(update.message().chat().username()).thenReturn("oposum");
        when(update.message().text()).thenReturn("/track");

        Dialog dialog = new Dialog(scrapperClient);
        SendMessage response = dialog.onUpdateReceived(update);
        String result = "Введите ссылку!";
        dialog.waitMap.put(2L, true);

        // then
        assertEquals(result, response.getParameters().get("text").toString());

        // when
        when(update.message().text()).thenReturn("https://github.com/aleckbb/tracking-bot");
        response = dialog.onUpdateReceived(update);
        result = "Ссылка добавлена для отслеживания!";

        // then
        assertEquals(result, response.getParameters().get("text").toString());
    }

    @Test
    @DisplayName("Тест команды /untrack")
    void test4() {
        // given
        Update update = mock(Update.class);
        server.start();
        stubFor(delete(urlEqualTo("/links")).willReturn(aResponse().withStatus(200)));
        stubFor(get(urlEqualTo("/links")).willReturn(aResponse().withStatus(200)));

        // when
        when(update.message()).thenReturn(mock(Message.class));
        when(update.message().chat()).thenReturn(mock(Chat.class));
        when(update.message().chat().id()).thenReturn(2L);
        when(update.message().chat().username()).thenReturn("oposum");
        when(update.message().text()).thenReturn("/untrack");

        Dialog dialog = new Dialog(scrapperClient);
        SendMessage response = dialog.onUpdateReceived(update);
        String result = "Введите ссылку!";

        // then
        assertEquals(result, response.getParameters().get("text").toString());

        // when
        when(update.message().text()).thenReturn("https://github.com/aleckbb/tracking-bot");
        response = dialog.onUpdateReceived(update);
        result = "Ссылка больше не отслеживается!";

        // then
        assertEquals(result, response.getParameters().get("text").toString());
    }

    @Test
    @DisplayName("Тест команды /list")
    void test5() throws MalformedURLException {
        // given
        Update update = mock(Update.class);
        server.start();
        stubFor(get(urlEqualTo("/links"))
            .withHeader("Tg-Chat-Id", WireMock.equalTo("2"))
            .willReturn(aResponse()
                .withStatus(200)));

        // when
        when(update.message()).thenReturn(mock(Message.class));
        when(update.message().chat()).thenReturn(mock(Chat.class));
        when(update.message().chat().id()).thenReturn(2L);
        when(update.message().chat().username()).thenReturn("oposum");
        when(update.message().text()).thenReturn("/list");

        Dialog dialog = new Dialog(scrapperClient);
        SendMessage response = dialog.onUpdateReceived(update);
        String result = "Вы ещё не отслеживаете ни одного сайта!";

        // then
        assertEquals(result, response.getParameters().get("text").toString());
    }

    @Test
    @DisplayName("Реакция бота на незнакомую команду")
    void test6() {
        // given
        Update update = mock(Update.class);

        // when
        when(update.message()).thenReturn(mock(Message.class));
        when(update.message().chat()).thenReturn(mock(Chat.class));
        when(update.message().chat().id()).thenReturn(2L);
        when(update.message().chat().username()).thenReturn("oposum");
        when(update.message().text()).thenReturn("/hello");

        Dialog dialog = new Dialog(scrapperClient);
        SendMessage response = dialog.onUpdateReceived(update);
        String result = "Я не знаю такой команды!";

        // then
        assertEquals(result, response.getParameters().get("text").toString());
    }

    @Test
    @DisplayName("Реакция бота на команду, если пользователь не зарегистрирован")
    void test7() {
        // given
        Update update = mock(Update.class);
        server.start();
        stubFor(post(urlEqualTo("/tg-chat/2")).willReturn(aResponse().withStatus(200)));
        stubFor(delete(urlEqualTo("/tg-chat/2")).willReturn(aResponse().withStatus(200)));

        // when
        when(update.message()).thenReturn(mock(Message.class));
        when(update.message().chat()).thenReturn(mock(Chat.class));
        when(update.message().chat().id()).thenReturn(2L);
        when(update.message().chat().username()).thenReturn("oposum");
        when(update.message().text()).thenReturn("/list");

        Dialog dialog = new Dialog(scrapperClient);
        SendMessage response = dialog.onUpdateReceived(update);
        String result = "Вы не зарегистрированы!";

        // then
        assertEquals(result, response.getParameters().get("text").toString());
    }
}
