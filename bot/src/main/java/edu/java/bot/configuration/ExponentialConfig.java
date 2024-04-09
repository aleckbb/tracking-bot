package edu.java.bot.configuration;

import edu.java.backOffPolicy.ExponentialBackOffPolicy;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.interceptor.RetryInterceptorBuilder;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "retry.retry-type", havingValue = "Exponential")
public class ExponentialConfig {
    @Bean(name = "MyInterceptor")
    @SuppressWarnings("MagicNumber")
    public RetryOperationsInterceptor exponentialPolicy(ApplicationConfig applicationConfig) {
        RetryOperationsInterceptor retryOperationsInterceptor =
            RetryInterceptorBuilder.StatelessRetryInterceptorBuilder.stateless().build();
        RetryTemplate retryTemplate = new RetryTemplate();
        RetryPolicy retryPolicy = new SimpleRetryPolicy(
            applicationConfig.retry().maxAttempts(),
            applicationConfig.retry().getMap()
        );
        retryTemplate.setRetryPolicy(retryPolicy);
        retryTemplate.setBackOffPolicy(new ExponentialBackOffPolicy(60_000L));
        retryOperationsInterceptor.setRetryOperations(retryTemplate);
        return retryOperationsInterceptor;
    }
}
