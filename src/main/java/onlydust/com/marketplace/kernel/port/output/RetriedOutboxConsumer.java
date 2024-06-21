package onlydust.com.marketplace.kernel.port.output;

import lombok.AllArgsConstructor;
import onlydust.com.marketplace.kernel.model.Event;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;

@AllArgsConstructor
public class RetriedOutboxConsumer implements OutboxConsumer {
    private final OutboxConsumer consumer;

    @Override
    public void process(Event event) {
        processWithRetry(event);
    }

    @Retryable(maxAttempts = 6, backoff = @Backoff(delay = 500, multiplier = 2))
    private void processWithRetry(Event event) {
        consumer.process(event);
    }
}
