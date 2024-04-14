package edu.java.bot.service;

import edu.java.models.Request.LinkUpdate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.stereotype.Service;

@Service
@EnableKafka
@Slf4j
public class BotQueueConsumer {
    @Autowired
    public UpdateSender updateSender;

    @RetryableTopic(attempts = "1", kafkaTemplate = "kafkaTemplate")
    @KafkaListener(
        topics = "${app.kafka.topic-name}",
        groupId = "group1",
        containerFactory = "kafkaListenerContainerFactory"
    )
    public void sendUpdates(LinkUpdate linkUpdate) {
        updateSender.sendUpdate(linkUpdate);
        log.info("message received");
    }

    @DltHandler
    public void handleUpdate(LinkUpdate linkUpdate) {
        log.info("message received to dlt");
    }
}
