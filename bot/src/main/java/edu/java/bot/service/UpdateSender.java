package edu.java.bot.service;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.models.Request.LinkUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UpdateSender {
    @Autowired
    private TrackingBot bot;

    public void sendUpdate(LinkUpdate linkUpdate) {
        for (long chatId : linkUpdate.tgChatIds()) {
            bot.sendUpdate(new SendMessage(chatId, linkUpdate.description()));
        }
    }
}
