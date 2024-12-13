package onlydust.com.marketplace.kernel.jobs;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import onlydust.com.marketplace.kernel.model.Event;
import onlydust.com.marketplace.kernel.model.EventType;
import onlydust.com.marketplace.kernel.port.output.OutboxConsumer;
import onlydust.com.marketplace.kernel.port.output.OutboxPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.concurrent.CompletableFuture.allOf;
import static java.util.concurrent.CompletableFuture.runAsync;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class OutboxAsyncConsumerJobTest {

    OutboxPortStub outbox;
    OutboxConsumer consumer;
    OutboxAsyncConsumerJob job;
    int maxConcurrency;

    @EventType("AsyncTestEvent")
    @EqualsAndHashCode(callSuper = false)
    @AllArgsConstructor
    public static class AsyncTestEvent extends Event {
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

    @RepeatedTest(10)
    void should_process_events_concurrently() throws InterruptedException {
        // Given
        final int eventCount = 5;
        final var assertionLatch = new CountDownLatch(1);
        final var processLatch = new CountDownLatch(eventCount);
        final var startLatch = new CountDownLatch(maxConcurrency);
        final Set<Long> concurrentEventIds = Collections.synchronizedSet(new HashSet<>());

        doAnswer(invocation -> {
            concurrentEventIds.add(((AsyncTestEvent) invocation.getArgument(0)).id);
            startLatch.countDown();
            assertionLatch.await(); // Simulate processing time
            processLatch.countDown();
            return null;
        }).when(consumer).process(any());

        // When
        for (long i = 1; i <= eventCount; i++) {
            newTestEvent(i);
        }
        final var runFuture = runAsync(() -> job.run());

        // Then
        assertThat(startLatch.await(5, TimeUnit.SECONDS)).isTrue();
        assertThat(concurrentEventIds).hasSize(maxConcurrency);
        assertionLatch.countDown();
        assertThat(processLatch.await(5, TimeUnit.SECONDS)).isTrue();
        assertThat(concurrentEventIds).hasSize(eventCount);

        runFuture.join();
        assertThat(job.getFutures()).hasSize(0);
        verify(outbox, times(eventCount + 1)).peek();
        verify(outbox, times(eventCount)).ack(anyLong());
        verify(consumer, times(eventCount)).process(any());
    }

    @RepeatedTest(10)
    void should_not_run_itself_concurrently() throws InterruptedException {
        // Given
        final AtomicInteger processCount = new AtomicInteger();
        doAnswer(invocation -> {
            processCount.incrementAndGet();
            return null;
        }).when(consumer).process(any());

        // When
        newTestEvent(1L);
        newTestEvent(2L);
        newTestEvent(3L);
        final var runFuture = allOf(runAsync(() -> job.run()), runAsync(() -> job.run()));

        // Then
        runFuture.join();
        assertThat(processCount.get()).isEqualTo(3);
        assertThat(job.getFutures()).isEmpty();
        verify(outbox).ack(eq(1L));
        verify(outbox).ack(eq(2L));
        verify(outbox).ack(eq(3L));
    }

    @RepeatedTest(10)
    void should_handle_processing_errors() throws InterruptedException {
        // Given
        final CountDownLatch processLatch = new CountDownLatch(2);
        doAnswer(invocation -> {
            AsyncTestEvent event = invocation.getArgument(0);
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
        assertThat(processLatch.await(5, TimeUnit.SECONDS)).isTrue();
        assertThat(job.getFutures()).isEmpty();
        verify(outbox).ack(eq(1L));
        verify(outbox).nack(eq(2L), eq("Test exception"));
        verify(outbox).ack(eq(3L));
    }

    @RepeatedTest(10)
    void should_handle_skipped_events() throws InterruptedException {
        // Given
        final CountDownLatch processLatch = new CountDownLatch(2);
        doAnswer(invocation -> {
            AsyncTestEvent event = invocation.getArgument(0);
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
        assertThat(processLatch.await(5, TimeUnit.SECONDS)).isTrue();
        assertThat(job.getFutures()).isEmpty();
        verify(outbox).ack(eq(1L));
        verify(outbox).skip(eq(2L), eq("Skip this event"));
        verify(outbox).ack(eq(3L));
    }

    @RepeatedTest(10)
    void should_prevent_a_single_event_from_being_processed_twice_concurrently() throws InterruptedException {
        // Given
        final AtomicInteger processCount = new AtomicInteger();
        doAnswer(invocation -> {
            Thread.sleep(500); // Simulate processing time
            processCount.incrementAndGet();
            return null;
        }).when(consumer).process(any());

        // When
        newTestEvent(1L);
        newTestEvent(1L);
        assertThatThrownBy(() -> job.run()).isInstanceOf(IllegalStateException.class);

        // Then
        assertThat(processCount.get()).isEqualTo(1);
        assertThat(job.getFutures()).isEmpty();
        verify(outbox).ack(eq(1L));
    }

    private void newTestEvent(final long id) {
        outbox.addEvent(new OutboxPort.IdentifiableEvent(id, new AsyncTestEvent(id)));
    }

    static class OutboxPortStub implements OutboxPort {
        private final Queue<IdentifiableEvent> events = new LinkedList<>();

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
        }

        @Override
        public void nack(Long eventId, String message) {
            System.out.println("Nacking event " + eventId + " because " + message);
        }

        @Override
        public void skip(Long eventId, String someReasonToSkip) {
            System.out.println("Skipping event " + eventId + " because " + someReasonToSkip);
        }
    }
}