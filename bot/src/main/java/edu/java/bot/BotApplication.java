package edu.java.bot;

import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.scrapperclient.ScrapperClient;
import edu.java.bot.service.TrackingBot;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(ApplicationConfig.class)
public class BotApplication {

    @Autowired
    ApplicationConfig applicationConfig;
    @Autowired ScrapperClient scrapperClient;

    public static void main(String[] args) {
        SpringApplication.run(BotApplication.class, args);
    }

    @PostConstruct
    public void runBot() {
        TrackingBot bot = new TrackingBot(applicationConfig.getTelegramToken(), scrapperClient);
        bot.run();
    }
}
