package edu.java.bot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.scrapperclient.ScrapperClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TrackingBot extends TelegramBot {
    private final Dialog dialog;

    @Autowired
    public TrackingBot(String botToken, ScrapperClient scrapperClient) {
        super(botToken);
        this.dialog = new Dialog(scrapperClient);
    }


    public void run() {
        this.setUpdatesListener(updates -> {
                updates.forEach(update -> execute(dialog.onUpdateReceived(update)));
                return UpdatesListener.CONFIRMED_UPDATES_ALL;
            }
        );
    }

    public void sendUpdate(SendMessage message) {
        execute(message);
    }
}
