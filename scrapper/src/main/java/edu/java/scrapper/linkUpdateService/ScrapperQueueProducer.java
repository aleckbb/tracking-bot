package edu.java.scrapper.linkUpdateService;

import edu.java.models.Request.LinkUpdate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;

@Slf4j
public class ScrapperQueueProducer implements LinkUpdateService {

    public final KafkaTemplate<String, LinkUpdate> kafkaTemplate;
    public final String topicName;

    public ScrapperQueueProducer(KafkaTemplate<String, LinkUpdate> kafkaTemplate, String topicName) {
        this.kafkaTemplate = kafkaTemplate;
        this.topicName = topicName;
    }

    @Override
    public void sendUpdate(LinkUpdate linkUpdate) {
        kafkaTemplate.send(topicName, linkUpdate).whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("massage was sent offset:{}", result.getRecordMetadata());
            } else {
                log.info("send error");
            }
        });
    }
}
