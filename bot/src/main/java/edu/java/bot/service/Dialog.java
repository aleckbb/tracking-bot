package edu.java.bot.service;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.scrapperclient.ScrapperClient;
import edu.java.models.Response.LinkResponse;
import edu.java.models.Response.ListLinksResponse;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@SuppressWarnings({"MultipleStringLiterals", "MagicNumber"})
@Component
public class Dialog {
    private final Pattern githubRegex = Pattern.compile("^https://github\\.com/[\\w-]+/[\\w-\\.@\\:~]+$");
    private final Pattern sofRegex = Pattern.compile("^https://stackoverflow\\.com/questions/\\d+/[\\w-\\.@\\:~]+$");
    public final Map<Long, Boolean> waitMap = new HashMap<>();

    public final ScrapperClient scrapperClient;

    @Autowired
    public Dialog(ScrapperClient scrapperClient) {
        this.scrapperClient = scrapperClient;
    }

    public SendMessage onUpdateReceived(Update update) {
        SendMessage message = new SendMessage(0, "");
        if (update != null && update.message() != null) {
            Chat chat = update.message().chat();
            String messageText = update.message().text();
            if (waitMap.containsKey(chat.id())) {
                message = waitMenu(chat, messageText);
            } else {
                switch (messageText) {
                    case "/start" -> {
                        message = start(chat);
                    }
                    case "/help" -> {
                        message = help(chat);
                    }
                    case "/track" -> {
                        message = track(chat);
                    }
                    case "/untrack" -> {
                        message = unTrack(chat);
                    }
                    case "/list" -> {
                        message = list(chat);
                    }
                    case null, default -> {
                        message = new SendMessage(chat.id(), "Я не знаю такой команды!");
                    }
                }
            }
        }
        return message;
    }

    public SendMessage waitMenu(Chat chat, String messageText) {
        SendMessage message;
        long id = chat.id();
        if (messageText.equals("/cancel")) {
            waitMap.remove(id);
            message = new SendMessage(id, "Ввод ссылок отменён!");
        } else {
            if (checkLink(messageText)) {
                message = waitMap.get(id) ? addLink(chat, messageText) : delLink(chat, messageText);
                waitMap.remove(id);
            } else {
                message = new SendMessage(id, "Неправильно введена ссылка!"
                    + " Вы можете ввести '/cancel' для отмены действия!");
            }
        }
        return message;
    }

    public boolean checkLink(String messageText) {
        try {
            if (messageText.contains("https://github.com/") && githubRegex.matcher(messageText).find()
                || messageText.contains("https://stackoverflow.com/") && sofRegex.matcher(messageText).find()) {
                URL link = new URL(messageText);
                HttpURLConnection connection = (HttpURLConnection) link.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                if (connection.getResponseCode() == 200) {
                    return true;
                }
            }
            throw new Exception();
        } catch (Exception e) {
            return false;
        }
    }

    public SendMessage addLink(Chat chat, String url) {
        try {
            scrapperClient.addLink(chat.id(), chat.username(), url);
            return new SendMessage(chat.id(), "Ссылка добавлена для отслеживания!");
        } catch (Exception e) {
            String answer = e.getCause().getMessage().equals("Too many requests")
                ? "Слишком много сообщений. Дай мне отдохнуть!" : "Ссылка уже отслеживается!";
            return new SendMessage(chat.id(), answer);
        }
    }

    public SendMessage delLink(Chat chat, String url) {
        try {
            scrapperClient.delLinks(chat.id(), url);
            return new SendMessage(chat.id(), "Ссылка больше не отслеживается!");
        } catch (Exception e) {
            String answer = e.getCause().getMessage().equals("Too many requests")
                ? "Слишком много сообщений. Дай мне отдохнуть!" : "Такой ссылки не отслеживается!";
            return new SendMessage(chat.id(), answer);
        }
    }

    public SendMessage start(Chat chat) {
        Long id = chat.id();
        String name = chat.username();
        try {
            if (scrapperClient.hasUser(id)) {
                return new SendMessage(id, "Ты уже зарегистрирован!");
            }
            scrapperClient.chatReg(id, name);
            return new SendMessage(id, "Привет, " + name + ", пометил тебя в блокнотике!");
        } catch (Exception e) {
            String answer = e.getCause().getMessage().equals("Too many requests")
                ? "Слишком много сообщений. Дай мне отдохнуть!" : e.getCause().getMessage();
            return new SendMessage(id, answer);
        }
    }

    public SendMessage help(Chat chat) {
        long id = chat.id();
        try {
            if (scrapperClient.hasUser(id)) {
                return new SendMessage(id, """
                    /start -- зарегистрировать пользователя
                    /help -- вывести окно с командами
                    /track -- начать отслеживание ссылки
                    /untrack -- прекратить отслеживание ссылки
                    /list -- показать список отслеживаемых ссылок""");
            } else {
                return new SendMessage(id, "Вы не зарегистрированы!");
            }
        } catch (Exception e) {
            String answer = e.getCause().getMessage().equals("Too many requests")
                ? "Слишком много сообщений. Дай мне отдохнуть!" : e.getCause().getMessage();
            return new SendMessage(id, answer);
        }
    }

    public SendMessage track(Chat chat) {
        long id = chat.id();
        try {
            if (scrapperClient.hasUser(id)) {
                waitMap.put(id, true);
                return new SendMessage(id, "Введите ссылку!");
            } else {
                return new SendMessage(id, "Вы не зарегистрированы!");
            }
        } catch (Exception e) {
            String answer = e.getCause().getMessage().equals("Too many requests")
                ? "Слишком много сообщений. Дай мне отдохнуть!" : e.getCause().getMessage();
            return new SendMessage(id, answer);
        }
    }

    public SendMessage unTrack(Chat chat) {
        long id = chat.id();
        try {
            if (scrapperClient.hasUser(id)) {
                try {
                    scrapperClient.getLinks(id);
                    waitMap.put(id, false);
                    return new SendMessage(id, "Введите ссылку!");
                } catch (Exception ex) {
                    return new SendMessage(id, "Вы ещё не отслеживаете ни одного сайта!");
                }
            } else {
                return new SendMessage(id, "Вы не зарегистрированы!");
            }
        } catch (Exception e) {
            String answer = e.getCause().getMessage().equals("Too many requests")
                ? "Слишком много сообщений. Дай мне отдохнуть!" : e.getCause().getMessage();
            return new SendMessage(id, answer);
        }
    }

    public SendMessage list(Chat chat) {
        long id = chat.id();
        try {
            if (scrapperClient.hasUser(id)) {
                try {
                    ListLinksResponse links = scrapperClient.getLinks(id);
                    StringBuilder messageText = new StringBuilder();
                    int i = 1;
                    for (LinkResponse link : links.links()) {
                        messageText.append(i).append(". ").append(link.url()).append("\n");
                        ++i;
                    }
                    return new SendMessage(id, messageText.toString());
                } catch (Exception ex) {
                    return new SendMessage(id, "Вы ещё не отслеживаете ни одного сайта!");
                }
            } else {
                return new SendMessage(id, "Вы не зарегистрированы!");
            }
        } catch (Exception e) {
            String answer = e.getCause().getMessage().equals("Too many requests")
                ? "Слишком много сообщений. Дай мне отдохнуть!" : e.getCause().getMessage();
            return new SendMessage(id, answer);
        }
    }
}
