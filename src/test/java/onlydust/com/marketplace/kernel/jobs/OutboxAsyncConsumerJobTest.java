package onlydust.com.marketplace.kernel.jobs;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import onlydust.com.marketplace.kernel.model.Event;
import onlydust.com.marketplace.kernel.model.EventType;
import onlydust.com.marketplace.kernel.port.output.OutboxConsumer;
import onlydust.com.marketplace.kernel.port.output.OutboxPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.concurrent.CompletableFuture.runAsync;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class OutboxAsyncConsumerJobTest {

    OutboxPortStub outbox;
    OutboxConsumer consumer;
    OutboxAsyncConsumerJob job;
    int maxConcurrency;

    @EventType("TestEvent")
    @EqualsAndHashCode(callSuper = false)
    @AllArgsConstructor
    public static class TestEvent extends Event {
        final long id;
    }

    @BeforeEach
    void setUp() {
        maxConcurrency = 3;
        outbox = spy(new OutboxPortStub());
        consumer = mock(OutboxConsumer.class);
        job = new OutboxAsyncConsumerJob(outbox, consumer, maxConcurrency);
    }

    @Test
    void should_do_nothing_when_there_is_no_event() {
        // When
        job.run();

        // Then
        verify(outbox).peek();
        verify(outbox, never()).ack(any());
        verify(outbox, never()).skip(any(), any());
        verify(outbox, never()).nack(any(), any());
        verify(consumer, never()).process(any());
    }

    @Test
    void should_process_events_concurrently() throws InterruptedException {
        // Given
        final int eventCount = 5;
        final CountDownLatch processLatch = new CountDownLatch(eventCount);
        final CountDownLatch startLatch = new CountDownLatch(maxConcurrency);
        final Set<Long> concurrentEventIds = Collections.synchronizedSet(new HashSet<>());

        doAnswer(invocation -> {
            startLatch.countDown();
            concurrentEventIds.add(((TestEvent) invocation.getArgument(0)).id);
            Thread.sleep(1000); // Simulate processing time
            processLatch.countDown();
            return null;
        }).when(consumer).process(any());

        // When
        for (long i = 1; i <= eventCount; i++) {
            newTestEvent(i);
        }
        runAsync(() -> job.run());

        // Then
        assertThat(startLatch.await(500, TimeUnit.MILLISECONDS)).isTrue();
        assertThat(concurrentEventIds).hasSize(maxConcurrency);
        assertThat(processLatch.await(2, TimeUnit.SECONDS)).isTrue();
        assertThat(concurrentEventIds).hasSize(eventCount);

        verify(outbox, times(eventCount)).ack(anyLong());
        verify(consumer, times(eventCount)).process(any());
    }

    @Test
    void should_handle_processing_errors() throws InterruptedException {
        // Given
        final CountDownLatch processLatch = new CountDownLatch(2);
        doAnswer(invocation -> {
            TestEvent event = invocation.getArgument(0);
            if (event.id == 2L) {
                throw new RuntimeException("Test exception");
            }
            processLatch.countDown();
            return null;
        }).when(consumer).process(any());

        // When
        newTestEvent(1L);
        newTestEvent(2L);
        newTestEvent(3L);
        job.run();

        // Then
        assertThat(processLatch.await(1, TimeUnit.SECONDS)).isTrue();
        Thread.sleep(10);
        verify(outbox).ack(eq(1L));
        verify(outbox).nack(eq(2L), eq("Test exception"));
        verify(outbox).ack(eq(3L));
    }

    @Test
    void should_handle_skipped_events() throws InterruptedException {
        // Given
        final CountDownLatch processLatch = new CountDownLatch(2);
        doAnswer(invocation -> {
            TestEvent event = invocation.getArgument(0);
            if (event.id == 2L) {
                throw new OutboxSkippingException("Skip this event");
            }
            processLatch.countDown();
            return null;
        }).when(consumer).process(any());

        // When
        newTestEvent(1L);
        newTestEvent(2L);
        newTestEvent(3L);
        job.run();

        // Then
        assertThat(processLatch.await(1, TimeUnit.SECONDS)).isTrue();
        Thread.sleep(10);
        verify(outbox).ack(eq(1L));
        verify(outbox).skip(eq(2L), eq("Skip this event"));
        verify(outbox).ack(eq(3L));
    }

    private TestEvent newTestEvent(final long id) {
        final var event = new TestEvent(id);
        outbox.addEvent(new OutboxPort.IdentifiableEvent(id, event));
        return event;
    }

    static class OutboxPortStub implements OutboxPort {
        private final Queue<IdentifiableEvent> events = new LinkedList<>();
        public final AtomicInteger ackCount = new AtomicInteger();
        public final AtomicInteger nackCount = new AtomicInteger();
        public final AtomicInteger skipCount = new AtomicInteger();

        public void addEvent(IdentifiableEvent event) {
            events.add(event);
        }

        @Override
        public void push(Event event) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Optional<IdentifiableEvent> peek() {
            return Optional.ofNullable(events.poll());
        }

        @Override
        public void ack(Long eventId) {
            System.out.println("Acknowledging event " + eventId);
            ackCount.incrementAndGet();
        }

        @Override
        public void nack(Long eventId, String message) {
            System.out.println("Nacking event " + eventId + " because " + message);
            nackCount.incrementAndGet();
        }

        @Override
        public void skip(Long eventId, String someReasonToSkip) {
            System.out.println("Skipping event " + eventId + " because " + someReasonToSkip);
            skipCount.incrementAndGet();
        }
    }
}