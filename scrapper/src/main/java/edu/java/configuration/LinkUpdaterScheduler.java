package edu.java.configuration;

import org.jboss.logging.Logger;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class LinkUpdaterScheduler {
    static final Logger LOGGER = Logger.getLogger(LinkUpdaterScheduler.class.getName());

    @Scheduled(fixedDelayString = "#{scheduler.interval}")
    public void update() {
        LOGGER.info("I'm updating!");
    }
}
