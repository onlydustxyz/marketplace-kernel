package onlydust.com.marketplace.kernel.jobs;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import onlydust.com.marketplace.kernel.port.output.OutboxConsumer;
import onlydust.com.marketplace.kernel.port.output.OutboxPort;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.util.concurrent.CompletableFuture.*;

@Slf4j
@RequiredArgsConstructor
public class OutboxAsyncConsumerJob {

    private final OutboxPort outbox;
    private final OutboxConsumer consumer;
    private final int maxConcurrency;
    @Getter(AccessLevel.PACKAGE) private final Map<Long, CompletableFuture<Void>> futures = Collections.synchronizedMap(new HashMap<>());
    private final AtomicBoolean isRunning = new AtomicBoolean(false);

    public void run() {
        if (!isRunning.compareAndSet(false, true)) {
            LOGGER.info("OutboxAsyncConsumerJob of %s is already running, skipping this execution".formatted(consumer.getClass().getSimpleName()));
            return;
        }

        try {
            Optional<OutboxPort.IdentifiableEvent> identifiableEvent;
            while ((identifiableEvent = outbox.peek()).isPresent()) {
                if (futures.size() >= maxConcurrency) {
                    anyOf(futures.values().toArray(CompletableFuture[]::new)).join(); // Will wait here if <maxConcurrency> commands are already being processed
                }
                final var e = identifiableEvent.get();
                ensureEventIsNotBeingProcessed(e.id());
                safelyPutFuture(e.id(), runAsync(() -> processEvent(e)));
            }
        } finally {
            isRunning.set(false);
            allOf(futures.values().toArray(CompletableFuture[]::new)).join(); // Ensure all futures are completed before finishing
        }
    }

    private void ensureEventIsNotBeingProcessed(Long eventId) {
        if (futures.containsKey(eventId)) {
            throw new IllegalStateException("Event %d is already being processed. The OutboxPort.peek() implementation should not return the same pending event twice.".formatted(eventId));
        }
    }

    private void safelyPutFuture(Long eventId, CompletableFuture<Void> future) {
        futures.put(eventId, future);
        if (future.isDone())
            futures.remove(eventId);
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
            }
        } finally {
            futures.remove(eventId);
        }
    }
}
