package edu.java.backOffPolicy;

import java.util.function.Supplier;
import org.springframework.retry.backoff.Sleeper;
import org.springframework.retry.backoff.SleepingBackOffPolicy;
import org.springframework.retry.backoff.StatelessBackOffPolicy;
import org.springframework.retry.backoff.ThreadWaitSleeper;

public abstract class MyBackOffPolicy<T extends MyBackOffPolicy<T>> extends StatelessBackOffPolicy
    implements SleepingBackOffPolicy<T> {

    protected static final long DEFAULT_BACK_OFF_PERIOD = 1000L;

    protected Supplier<Long> backOffPeriod = () -> DEFAULT_BACK_OFF_PERIOD;

    protected Sleeper sleeper = new ThreadWaitSleeper();

    public MyBackOffPolicy() {
    }

    public void setSleeper(Sleeper sleeper) {
        this.sleeper = sleeper;
    }

    public void setBackOffPeriod(long backOffPeriod) {
        this.backOffPeriod = () -> backOffPeriod > 0L ? backOffPeriod : 1L;
    }
}
