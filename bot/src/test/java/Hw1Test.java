import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.scrapperclient.ScrapperClient;
import edu.java.bot.service.Dialog;
import java.net.MalformedURLException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class Hw1Test {

    @BeforeEach
    void deleteOposum() {
        ScrapperClient scrapperClient = new ScrapperClient(WebClient.builder(), "http://localhost:8080");
        try {
            scrapperClient.chatDel(2L);
        } catch (Exception e) {

        }
    }

    @Test
    @DisplayName("Тест команды /start")
    void test1() {

        // given
        Update update = mock(Update.class);

        // when
        when(update.message()).thenReturn(mock(Message.class));
        when(update.message().chat()).thenReturn(mock(Chat.class));
        when(update.message().chat().id()).thenReturn(2L);
        when(update.message().chat().username()).thenReturn("oposum");
        when(update.message().text()).thenReturn("/start");

        ScrapperClient scrapperClient = new ScrapperClient(WebClient.builder(), "http://localhost:8080");
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

        ScrapperClient scrapperClient = new ScrapperClient(WebClient.builder(), "http://localhost:8080");
        Dialog dialog = new Dialog(scrapperClient);
        dialog.scrapperClient.chatReg(2L, "oposum");
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

        // when
        when(update.message()).thenReturn(mock(Message.class));
        when(update.message().chat()).thenReturn(mock(Chat.class));
        when(update.message().chat().id()).thenReturn(2L);
        when(update.message().chat().username()).thenReturn("oposum");
        when(update.message().text()).thenReturn("/track");

        ScrapperClient scrapperClient = new ScrapperClient(WebClient.builder(), "http://localhost:8080");
        Dialog dialog = new Dialog(scrapperClient);
        dialog.scrapperClient.chatReg(2L, "oposum");
        SendMessage response = dialog.onUpdateReceived(update);
        String result = "Введите ссылку!";

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
    void test4() throws MalformedURLException {
        // given
        Update update = mock(Update.class);

        // when
        when(update.message()).thenReturn(mock(Message.class));
        when(update.message().chat()).thenReturn(mock(Chat.class));
        when(update.message().chat().id()).thenReturn(2L);
        when(update.message().chat().username()).thenReturn("oposum");
        when(update.message().text()).thenReturn("/untrack");

        ScrapperClient scrapperClient = new ScrapperClient(WebClient.builder(), "http://localhost:8080");
        Dialog dialog = new Dialog(scrapperClient);
        dialog.scrapperClient.chatReg(2L, "oposum");
        dialog.scrapperClient.addLink(2L, "oposum", "https://github.com/aleckbb/tracking-bot");
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

        // when
        when(update.message()).thenReturn(mock(Message.class));
        when(update.message().chat()).thenReturn(mock(Chat.class));
        when(update.message().chat().id()).thenReturn(2L);
        when(update.message().chat().username()).thenReturn("oposum");
        when(update.message().text()).thenReturn("/list");

        ScrapperClient scrapperClient = new ScrapperClient(WebClient.builder(), "http://localhost:8080");
        Dialog dialog = new Dialog(scrapperClient);
        dialog.scrapperClient.chatReg(2L, "oposum");
        dialog.scrapperClient.addLink(2L, "oposum", "https://github.com/aleckbb/tracking-bot");
        SendMessage response = dialog.onUpdateReceived(update);
        String result = "1. https://github.com/aleckbb/tracking-bot\n";

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

        ScrapperClient scrapperClient = new ScrapperClient(WebClient.builder(), "http://localhost:8080");
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

        // when
        when(update.message()).thenReturn(mock(Message.class));
        when(update.message().chat()).thenReturn(mock(Chat.class));
        when(update.message().chat().id()).thenReturn(2L);
        when(update.message().chat().username()).thenReturn("oposum");
        when(update.message().text()).thenReturn("/list");

        ScrapperClient scrapperClient = new ScrapperClient(WebClient.builder(), "http://localhost:8080");
        Dialog dialog = new Dialog(scrapperClient);
        SendMessage response = dialog.onUpdateReceived(update);
        String result = "Вы не зарегистрированы!";

        // then
        assertEquals(result, response.getParameters().get("text").toString());
    }
}
