package edu.java.configuration;

import edu.java.backOffPolicy.LinearBackOffPolicy;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.interceptor.RetryInterceptorBuilder;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;
import org.springframework.retry.support.RetryTemplate;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "retry-type", havingValue = "Linear")
public class LinearConfig {
    @Bean(name = "MyInterceptor")
    public RetryOperationsInterceptor linearPolicy() {
        RetryOperationsInterceptor retryOperationsInterceptor =
            RetryInterceptorBuilder.StatelessRetryInterceptorBuilder.stateless().build();
        RetryTemplate retryTemplate = new RetryTemplate();
        retryTemplate.setBackOffPolicy(new LinearBackOffPolicy());
        retryOperationsInterceptor.setRetryOperations(retryTemplate);
        return retryOperationsInterceptor;
    }
}
