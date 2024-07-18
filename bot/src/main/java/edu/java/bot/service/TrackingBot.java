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

    private final CustomCounter counter;

    @Autowired
    public TrackingBot(String botToken, ScrapperClient scrapperClient, CustomCounter counter) {
        super(botToken);
        this.dialog = new Dialog(scrapperClient);
        this.counter = counter;
    }

    public void run() {
        this.setUpdatesListener(updates -> {
                updates.forEach(update -> {
                    execute(dialog.onUpdateReceived(update));
                    counter.getCounter().increment();
                });
                return UpdatesListener.CONFIRMED_UPDATES_ALL;
            }
        );
    }

    public void sendUpdate(SendMessage message) {
        execute(message);
    }
}
