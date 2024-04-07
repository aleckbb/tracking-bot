package edu.java.bot.configuration;

import edu.java.backOffPolicy.ConstBackOffPolicy;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.interceptor.RetryInterceptorBuilder;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;
import org.springframework.retry.support.RetryTemplate;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "retry-type", havingValue = "Const")
public class ConstConfig {
    @Bean(name = "MyInterceptor")
    public RetryOperationsInterceptor constPolicy() {
        RetryOperationsInterceptor retryOperationsInterceptor =
            RetryInterceptorBuilder.StatelessRetryInterceptorBuilder.stateless().build();
        RetryTemplate retryTemplate = new RetryTemplate();
        retryTemplate.setBackOffPolicy(new ConstBackOffPolicy());
        retryOperationsInterceptor.setRetryOperations(retryTemplate);
        return retryOperationsInterceptor;
    }
}