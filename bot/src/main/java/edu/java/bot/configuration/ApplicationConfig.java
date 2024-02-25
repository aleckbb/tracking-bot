package edu.java.bot.configuration;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;

@Validated
@Data
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public class ApplicationConfig {

    @NotEmpty
    private final String telegramToken;

    @Bean
    public String getTelegramToken() {
        return telegramToken;
    }

}
