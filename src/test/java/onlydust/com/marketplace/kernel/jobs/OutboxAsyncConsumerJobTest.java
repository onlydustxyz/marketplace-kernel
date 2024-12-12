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
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.CompletableFuture.runAsync;
import static org.assertj.core.api.Assertions.assertThat;
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

    @Test
    void should_process_events_concurrently() throws InterruptedException {
        // Given
        final int eventCount = 5;
        final CountDownLatch assertionLatch = new CountDownLatch(1);
        final CountDownLatch processLatch = new CountDownLatch(eventCount);
        final CountDownLatch startLatch = new CountDownLatch(maxConcurrency);
        final Set<Long> concurrentEventIds = Collections.synchronizedSet(new HashSet<>());

        doAnswer(invocation -> {
            startLatch.countDown();
            concurrentEventIds.add(((AsyncTestEvent) invocation.getArgument(0)).id);
            assertionLatch.await(); // Simulate processing time
            processLatch.countDown();
            return null;
        }).when(consumer).process(any());

        // When
        for (long i = 1; i <= eventCount; i++) {
            newTestEvent(i);
        }
        runAsync(() -> job.run());
        runAsync(() -> job.run()); // Check that running the job multiple times doesn't process the same event twice

        // Then
        assertThat(startLatch.await(5, TimeUnit.SECONDS)).isTrue();
        assertThat(concurrentEventIds).hasSize(maxConcurrency);
        assertionLatch.countDown();
        assertThat(processLatch.await(5, TimeUnit.SECONDS)).isTrue();
        assertThat(concurrentEventIds).hasSize(eventCount);

        verify(outbox, times(eventCount + 1)).peek();
        verify(outbox, times(eventCount)).ack(anyLong());
        verify(consumer, times(eventCount)).process(any());
    }

    @Test
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
        Thread.sleep(10);
        verify(outbox).ack(eq(1L));
        verify(outbox).nack(eq(2L), eq("Test exception"));
        verify(outbox).ack(eq(3L));
    }

    @Test
    void should_handle_skipped_events() throws InterruptedException {
        // Given
        System.out.println("should_handle_skipped_events start");
        final CountDownLatch processLatch = new CountDownLatch(2);
        doAnswer(invocation -> {
            AsyncTestEvent event = invocation.getArgument(0);
            System.out.println("Processing event " + event.id);
            if (event.id == 2L) {
                throw new OutboxSkippingException("Skip this event");
            }
            processLatch.countDown();
            System.out.println("processLatch.countDown(); done");
            return null;
        }).when(consumer).process(any());

        // When
        newTestEvent(1L);
        newTestEvent(2L);
        newTestEvent(3L);
        job.run();

        // Then
        System.out.println("before awaitQuiescence");
        ForkJoinPool.commonPool().awaitQuiescence(5, TimeUnit.SECONDS);
        System.out.println("after awaitQuiescence");
        assertThat(processLatch.await(5, TimeUnit.SECONDS)).isTrue();
        System.out.println("after processLatch.await");
        verify(outbox).ack(eq(1L));
        verify(outbox).skip(eq(2L), eq("Skip this event"));
        verify(outbox).ack(eq(3L));
        System.out.println("should_handle_skipped_events end");
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