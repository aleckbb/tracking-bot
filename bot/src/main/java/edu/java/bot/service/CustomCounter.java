package edu.java.bot.service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CustomCounter {
    @Autowired MeterRegistry meterRegistry;

    public Counter getCounter() {
        return Counter.builder("counter").register(meterRegistry);
    }
}
