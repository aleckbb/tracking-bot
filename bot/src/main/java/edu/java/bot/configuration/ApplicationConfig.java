package edu.java.bot.configuration;

import jakarta.validation.constraints.NotEmpty;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
    @NotEmpty
    String telegramToken,
    Retry retry,
    Boolean useQueue,
    Kafka kafka
) {
    @Bean
    public String getTelegramToken() {
        return telegramToken;
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
