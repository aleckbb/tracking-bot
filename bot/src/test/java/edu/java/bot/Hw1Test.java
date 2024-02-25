package edu.java.bot;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.service.Dialog;
import edu.java.bot.user.User;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class Hw1Test {
    @Test
    @DisplayName("Тест команды /start")
    void test1() {

        // given
        Update update = mock(Update.class);

        // when
        when(update.message()).thenReturn(mock(Message.class));
        when(update.message().chat()).thenReturn(mock(Chat.class));
        when(update.message().chat().id()).thenReturn(1L);
        when(update.message().text()).thenReturn("/start");

        Dialog dialog = new Dialog();
        ArrayList<User> users = new ArrayList<>();
        SendMessage response = dialog.onUpdateReceived(update, users);
        String result = "Привет, null, пометил тебя в блокнотике!";

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
        when(update.message().text()).thenReturn("/help");

        Dialog dialog = new Dialog();
        ArrayList<User> users = new ArrayList<>();
        users.add(new User(null, 1L, null, null));
        SendMessage response = dialog.onUpdateReceived(update, users);
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
        when(update.message().chat().id()).thenReturn(1L);
        when(update.message().text()).thenReturn("/track");

        Dialog dialog = new Dialog();
        ArrayList<User> users = new ArrayList<>();
        users.add(new User(null, 1L, null, null));
        SendMessage response = dialog.onUpdateReceived(update, users);
        String result = "Введите ссылку!";

        // then
        assertEquals(result, response.getParameters().get("text").toString());

        // when
        when(update.message().chat().id()).thenReturn(2L);
        when(update.message().text()).thenReturn("https://github.com/aleckbb/tracking-bot/pull/1");
        users.add(new User(null, 2L, new ArrayList<>(), true));
        response = dialog.onUpdateReceived(update, users);
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
        when(update.message().chat().id()).thenReturn(1L);
        when(update.message().text()).thenReturn("/untrack");

        Dialog dialog = new Dialog();
        ArrayList<User> users = new ArrayList<>();
        ArrayList<URL> tracks = new ArrayList<>();
        tracks.add(new URL("https://github.com/aleckbb/tracking-bot/pull/1"));
        users.add(new User(null, 1L, tracks, null));
        SendMessage response = dialog.onUpdateReceived(update, users);
        String result = "Введите ссылку!";

        // then
        assertEquals(result, response.getParameters().get("text").toString());

        // when
        when(update.message().chat().id()).thenReturn(2L);
        when(update.message().text()).thenReturn("https://github.com/aleckbb/tracking-bot/pull/1");
        users.add(new User(null, 2L, tracks, false));
        response = dialog.onUpdateReceived(update, users);
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
        when(update.message().chat().id()).thenReturn(1L);
        when(update.message().text()).thenReturn("/list");

        Dialog dialog = new Dialog();
        ArrayList<User> users = new ArrayList<>();
        ArrayList<URL> tracks = new ArrayList<>();
        tracks.add(new URL("https://github.com/aleckbb/tracking-bot/pull/1"));
        users.add(new User(null, 1L, tracks, null));
        SendMessage response = dialog.onUpdateReceived(update, users);
        String result = "1. https://github.com/aleckbb/tracking-bot/pull/1\n";

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
        when(update.message().chat().id()).thenReturn(1L);
        when(update.message().text()).thenReturn("/hello");

        Dialog dialog = new Dialog();
        ArrayList<User> users = new ArrayList<>();
        users.add(new User(null, 1L, null, null));
        SendMessage response = dialog.onUpdateReceived(update, users);
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
        when(update.message().chat().id()).thenReturn(1L);
        when(update.message().text()).thenReturn("/list");

        Dialog dialog = new Dialog();
        ArrayList<User> users = new ArrayList<>();
        SendMessage response = dialog.onUpdateReceived(update, users);
        String result = "Вы не зарегистрированы!";

        // then
        assertEquals(result, response.getParameters().get("text").toString());
    }
}
