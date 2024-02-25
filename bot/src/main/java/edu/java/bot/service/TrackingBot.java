package edu.java.bot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import edu.java.bot.user.User;
import java.util.ArrayList;
import org.springframework.stereotype.Component;

@Component
public class TrackingBot extends TelegramBot {

    public TrackingBot(String botToken) {
        super(botToken);
    }

    private final Dialog dialog = new Dialog();
    private final ArrayList<User> users = new ArrayList<>();

    public void run() {
        this.setUpdatesListener(updates -> {
                updates.forEach(update -> execute(dialog.onUpdateReceived(update, users)));
                return UpdatesListener.CONFIRMED_UPDATES_ALL;
            }
        );
    }
}
