package onlydust.com.marketplace.kernel.port.output;

import onlydust.com.marketplace.kernel.model.Event;

import java.util.List;

public class OutboxConsumerComposite implements OutboxConsumer {
    private final List<OutboxConsumer> consumers;

    public OutboxConsumerComposite(OutboxConsumer... consumers) {
        this.consumers = List.of(consumers);
    }

    @Override
    public void process(Event event) {
        consumers.forEach(consumer -> consumer.process(event));
    }
}
