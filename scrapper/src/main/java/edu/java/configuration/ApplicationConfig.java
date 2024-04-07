package edu.java.configuration;

import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
    @Bean
    Scheduler scheduler,
    @NotNull
    String baseUrlGithub,
    @NotNull
    String baseUrlStackOverFlow,
    AccessType databaseAccessType,

    RetryType retryType
) {
    public record Scheduler(boolean enable, @NotNull Duration interval, @NotNull Duration forceCheckDelay) {
    }

    public enum AccessType {
        JDBC, JPA
    }

    public enum RetryType {
        Const, Linear, Exponential
    }
}
