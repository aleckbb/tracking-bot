package edu.java.scrapper.configuration;

import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
    @Bean
    Scheduler scheduler,

    Retry retry,
    @NotNull
    String baseUrlGithub,
    @NotNull
    String baseUrlStackOverFlow,
    AccessType databaseAccessType,
    Boolean useQueue,
    @Bean
    Kafka kafka
) {
    public record Scheduler(boolean enable, @NotNull Duration interval, @NotNull Duration forceCheckDelay) {
    }

    public enum AccessType {
        JDBC, JPA
    }

    public enum RetryType {
        Const, Linear, Exponential
    }

    public record Retry(RetryType retryType, int maxAttempts, List<String> retryableExceptions) {
        public Map<Class<? extends Throwable>, Boolean> getMap() {
            List<? extends Class<? extends Throwable>> keyList =
                retryableExceptions.stream().map((String className) -> {
                    try {
                        return (Class<? extends Throwable>) (Class.forName(className));
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }).toList();
            Map<Class<? extends Throwable>, Boolean> trueRetryableExceptions = new HashMap<>();
            for (Class<? extends Throwable> aClass : keyList) {
                trueRetryableExceptions.put(aClass, true);
            }
            return trueRetryableExceptions;
        }
    }

    public record Kafka(String bootstrapServer, String topicName, int partitionsCount, short replicationCount) {

    }
}
