package edu.java.backOffPolicy;

import org.jboss.logging.Logger;
import org.springframework.retry.backoff.BackOffInterruptedException;
import org.springframework.retry.backoff.Sleeper;

@SuppressWarnings("MagicNumber")
public class ConstBackOffPolicy extends MyBackOffPolicy<ConstBackOffPolicy> {
    static final Logger LOGGER = Logger.getLogger(ConstBackOffPolicy.class.getName());

    public ConstBackOffPolicy() {
    }

    @Override protected void doBackOff() throws BackOffInterruptedException {
        try {
            LOGGER.info(String.format("Retry in %s seconds", this.backOffPeriod.get() / 1000L));
            this.sleeper.sleep(this.backOffPeriod.get());
        } catch (InterruptedException e) {
            throw new BackOffInterruptedException("Thread interrupted while sleeping", e);
        }
    }

    @Override public ConstBackOffPolicy withSleeper(Sleeper sleeper) {
        ConstBackOffPolicy res = new ConstBackOffPolicy();
        res.setBackOffPeriod(this.backOffPeriod.get());
        res.setSleeper(sleeper);
        return res;
    }
}
