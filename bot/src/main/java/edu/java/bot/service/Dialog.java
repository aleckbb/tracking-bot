package edu.java.bot.service;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.user.User;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import org.springframework.stereotype.Component;

@Component
@SuppressWarnings("MultipleStringLiterals")
public class Dialog {
    public SendMessage onUpdateReceived(Update update, ArrayList<User> users) {
        SendMessage message = new SendMessage(0, "");

        if (update != null && update.message() != null) {
            long id = update.message().chat().id();
            User user = getUser(id, users);
            String messageText = update.message().text();
            if (user != null && user.isWaitLink() != null) {
                message = waitMenu(user, messageText);
            } else {
                switch (messageText) {
                    case "/start" -> {
                        message = start(update.message().chat(), users);
                    }
                    case "/help" -> {
                        message = help(id, users);
                    }
                    case "/track" -> {
                        message = track(id, users);
                    }
                    case "/untrack" -> {
                        message = untrack(id, users);
                    }
                    case "/list" -> {
                        message = list(id, users);
                    }
                    default -> {
                        message = new SendMessage(id, "Я не знаю такой команды!");
                    }
                }
            }
        }
        return message;
    }

    public SendMessage waitMenu(User user, String messageText) {
        SendMessage message;
        if (messageText.equals("/cancel")) {
            user.setWaitLink(null);
            message = new SendMessage(user.id(), "Ввод ссылок отменён!");
        } else {
            try {
                if (messageText.contains("https://github.com/")
                    || messageText.contains("https://stackoverflow.com/")) {
                    URL url = new URL(messageText);
                    if (user.isWaitLink()) {
                        message = addLink(user, url);
                    } else {
                        message = delLink(user, url);
                    }
                } else {
                    throw new MalformedURLException();
                }
            } catch (MalformedURLException e) {
                message = new SendMessage(user.id(), "Неправильно введена ссылка!"
                    + " Вы можете ввести '/cancel' для отмены действия!");
            }
        }
        return message;
    }

    public SendMessage addLink(User user, URL url) {
        SendMessage message;
        user.setWaitLink(null);
        if (hasURL(user.tracks(), url)) {
            message = new SendMessage(user.id(), "Вы уже отслеживаете эту ссылку!");
        } else {
            user.tracks().add(url);
            message = new SendMessage(user.id(), "Ссылка добавлена для отслеживания!");
        }
        return message;
    }

    public SendMessage delLink(User user, URL url) {
        SendMessage message;
        user.setWaitLink(null);
        if (!hasURL(user.tracks(), url)) {
            message = new SendMessage(user.id(), "Вы ещё не отслеживаете эту ссылку!");
        } else {
            user.tracks().remove(getLink(user.tracks(), url));
            message = new SendMessage(user.id(), "Ссылка больше не отслеживается!");
        }
        return message;
    }

    public SendMessage start(Chat chat, ArrayList<User> users) {
        Long id = chat.id();
        User user = getUser(id, users);
        if (user != null) {
            SendMessage message = new SendMessage(user.id(), "Вы уже зарегистрированы!");
            return message;
        }
        String name = chat.firstName();
        users.add(new User(name, id, new ArrayList<URL>(), null));
        return new SendMessage(id, "Привет, " + name + ", пометил тебя в блокнотике!");
    }

    public SendMessage help(long id, ArrayList<User> users) {
        return getUser(id, users) == null ? new SendMessage(id, "Вы не зарегистрированы!")
            : new SendMessage(id, """
            /start -- зарегистрировать пользователя
            /help -- вывести окно с командами
            /track -- начать отслеживание ссылки
            /untrack -- прекратить отслеживание ссылки
            /list -- показать список отслеживаемых ссылок""");
    }

    public SendMessage track(long id, ArrayList<User> users) {
        User user = getUser(id, users);
        if (user != null) {
            user.setWaitLink(true);
            SendMessage message = new SendMessage(user.id(), "Введите ссылку!");
            return message;
        } else {
            return new SendMessage(id, "Вы не зарегистрированы!");
        }
    }

    public SendMessage untrack(long id, ArrayList<User> users) {
        User user = getUser(id, users);
        if (user != null) {
            if (user.tracks().isEmpty()) {
                return new SendMessage(user.id(), "Вы ещё не отслеживаете ни одного сайта!");
            }
            user.setWaitLink(false);
            return new SendMessage(user.id(), "Введите ссылку!");
        } else {
            return new SendMessage(id, "Вы не зарегистрированы!");
        }
    }

    public SendMessage list(long id, ArrayList<User> users) {
        User user = getUser(id, users);
        if (user != null) {
            StringBuilder messageText = new StringBuilder();
            if (!user.tracks().isEmpty()) {
                int i = 1;
                ArrayList<URL> tracks = user.tracks();
                for (URL track : tracks) {
                    messageText.append(i).append(". ").append(track.toString()).append("\n");
                    ++i;
                }
                return new SendMessage(user.id(), messageText.toString());
            } else {
                return new SendMessage(user.id(), "Вы ещё не отслеживаете ни одного сайта!");
            }
        } else {
            return new SendMessage(id, "Вы не зарегистрированы!");
        }
    }

    public URL getLink(ArrayList<URL> urls, URL newURL) {
        for (URL url : urls) {
            if (url.toString().equals(newURL.toString())) {
                return url;
            }
        }
        return null;
    }

    public boolean hasURL(ArrayList<URL> urls, URL newURL) {
        for (URL url : urls) {
            if (url.toString().equals(newURL.toString())) {
                return true;
            }
        }
        return false;
    }

    public User getUser(long id, ArrayList<User> users) {
        for (User user : users) {
            if (user.id() == id) {
                return user;
            }
        }
        return null;
    }
}
