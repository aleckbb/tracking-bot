package edu.java.backOffPolicy;

import org.jboss.logging.Logger;
import org.springframework.retry.backoff.BackOffInterruptedException;
import org.springframework.retry.backoff.Sleeper;

@SuppressWarnings("MagicNumber")
public class LinearBackOffPolicy extends MyBackOffPolicy<LinearBackOffPolicy> {

    static final Logger LOGGER = Logger.getLogger(ConstBackOffPolicy.class.getName());

    public LinearBackOffPolicy() {
    }

    @Override
    protected void doBackOff() throws BackOffInterruptedException {
        try {
            LOGGER.info(String.format("Retry in %s seconds", this.backOffPeriod.get() / 1000L));
            this.sleeper.sleep(this.backOffPeriod.get());
            this.setBackOffPeriod(this.backOffPeriod.get() + DEFAULT_BACK_OFF_PERIOD);
        } catch (InterruptedException e) {
            throw new BackOffInterruptedException("Thread interrupted while sleeping", e);
        }
    }

    @Override
    public LinearBackOffPolicy withSleeper(Sleeper sleeper) {
        LinearBackOffPolicy res = new LinearBackOffPolicy();
        res.setBackOffPeriod(this.backOffPeriod.get());
        res.setSleeper(sleeper);
        return res;
    }
}
