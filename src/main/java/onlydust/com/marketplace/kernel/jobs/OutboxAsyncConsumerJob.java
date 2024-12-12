package onlydust.com.marketplace.kernel.jobs;

import lombok.extern.slf4j.Slf4j;
import onlydust.com.marketplace.kernel.port.output.OutboxConsumer;
import onlydust.com.marketplace.kernel.port.output.OutboxPort;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

import static onlydust.com.marketplace.kernel.exception.OnlyDustException.internalServerError;

@Slf4j
public class OutboxAsyncConsumerJob {

    private final OutboxPort outbox;
    private final OutboxConsumer consumer;
    private final Semaphore semaphore;
    private final AtomicBoolean isRunning = new AtomicBoolean(false);

    public OutboxAsyncConsumerJob(OutboxPort outbox, OutboxConsumer consumer, int maxConcurrency) {
        this.outbox = outbox;
        this.consumer = consumer;
        this.semaphore = new Semaphore(maxConcurrency);
    }

    public void run() {
        if (!isRunning.compareAndSet(false, true)) {
            LOGGER.info("OutboxAsyncConsumerJob of %s is already running, skipping this execution".formatted(consumer.getClass().getSimpleName()));
            return;
        }

        try {
            Optional<OutboxPort.IdentifiableEvent> identifiableEvent;
            while ((identifiableEvent = outbox.peek()).isPresent()) {
                try {
                    semaphore.acquire(); // Will wait here if <maxConcurrency> commands are already being processed
                    final var e = identifiableEvent;
                    CompletableFuture.runAsync(() -> processEvent(e.get()));
                } catch (InterruptedException e) {
                    throw internalServerError("Interrupted while waiting to process command");
                }
            }
        } finally {
            isRunning.set(false);
        }
    }

    private void processEvent(OutboxPort.IdentifiableEvent identifiableEvent) {
        final var eventId = identifiableEvent.id();
        try {
            consumer.process(identifiableEvent.event());
            outbox.ack(eventId);
        } catch (Exception e) {
            if (e instanceof OutboxSkippingException) {
                LOGGER.warn("Skipping event %d".formatted(eventId), e);
                outbox.skip(eventId, e.getMessage());
            } else {
                LOGGER.error("Error while processing event %d".formatted(eventId), e);
                outbox.nack(eventId, e.getMessage());
                throw e;
            }
        } finally {
            semaphore.release();
        }
    }
}
