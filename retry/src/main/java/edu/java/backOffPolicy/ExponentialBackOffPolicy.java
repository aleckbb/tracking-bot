package edu.java.backOffPolicy;

import org.jboss.logging.Logger;
import org.springframework.retry.backoff.BackOffInterruptedException;
import org.springframework.retry.backoff.Sleeper;

@SuppressWarnings("MagicNumber")
public class ExponentialBackOffPolicy extends MyBackOffPolicy<ExponentialBackOffPolicy> {

    static final Logger LOGGER = Logger.getLogger(ExponentialBackOffPolicy.class.getName());
    private static final long MULTIPLIER = 2L;
    private final long maxPeriod;

    public ExponentialBackOffPolicy(long maxPeriod) {
        this.maxPeriod = maxPeriod;
    }

    @Override
    protected void doBackOff() throws BackOffInterruptedException {
        try {
            LOGGER.info(String.format("Retry in %s seconds", this.backOffPeriod.get() / 1000L));
            this.sleeper.sleep(this.backOffPeriod.get());
            this.setBackOffPeriod(Math.min(this.backOffPeriod.get() * MULTIPLIER, maxPeriod));
        } catch (InterruptedException e) {
            throw new BackOffInterruptedException("Thread interrupted while sleeping", e);
        }
    }

    @Override
    public ExponentialBackOffPolicy withSleeper(Sleeper sleeper) {
        ExponentialBackOffPolicy res = new ExponentialBackOffPolicy(this.maxPeriod);
        res.setBackOffPeriod(this.backOffPeriod.get());
        res.setSleeper(sleeper);
        return res;
    }
}
