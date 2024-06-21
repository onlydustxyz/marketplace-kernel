package onlydust.com.marketplace.kernel.port.output;

import lombok.AllArgsConstructor;
import onlydust.com.marketplace.kernel.jobs.OutboxSkippingException;
import onlydust.com.marketplace.kernel.model.Event;

@AllArgsConstructor
public class SkippedOnFailureOutboxConsumer implements OutboxConsumer {
    private final OutboxConsumer consumer;

    @Override
    public void process(Event event) {
        try {
            consumer.process(event);
        } catch (Exception e) {
            throw OutboxSkippingException.of(e);
        }
    }
}
