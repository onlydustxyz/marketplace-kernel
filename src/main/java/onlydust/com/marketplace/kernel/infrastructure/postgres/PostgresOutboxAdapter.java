package onlydust.com.marketplace.kernel.infrastructure.postgres;

import lombok.AllArgsConstructor;
import onlydust.com.marketplace.kernel.model.Event;
import onlydust.com.marketplace.kernel.port.output.OutboxPort;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static onlydust.com.marketplace.kernel.exception.OnlyDustException.internalServerError;

@AllArgsConstructor
public class PostgresOutboxAdapter<E extends EventEntity> implements OutboxPort {

    private final OutboxRepository<E> outboxRepository;

    @Override
    @Transactional
    public void push(Event event) {
        outboxRepository.saveEvent(event);
    }

    @Override
    @Transactional
    public Optional<IdentifiableEvent> peek() {
        return outboxRepository.findNextToProcess()
                .map(e -> {
                    e.setStatus(EventEntity.Status.PROCESSING);
                    outboxRepository.saveAndFlush(e);
                    return e;
                })
                .map(EventEntity::toIdentifiableEvent);
    }

    @Override
    @Transactional
    public void ack(Long eventId) {
        final var entity = outboxRepository.selectForUpdateById(eventId)
                .orElseThrow(() -> internalServerError("Event %d not found".formatted(eventId)));

        entity.setStatus(EventEntity.Status.PROCESSED);
        entity.setError(null);
        outboxRepository.saveAndFlush(entity);
    }

    @Override
    @Transactional
    public void nack(Long eventId, String message) {
        final var entity = outboxRepository.selectForUpdateById(eventId)
                .orElseThrow(() -> internalServerError("Event %d not found".formatted(eventId)));

        entity.setStatus(EventEntity.Status.FAILED);
        entity.setError(message);
        outboxRepository.saveAndFlush(entity);
    }

    @Override
    @Transactional
    public void skip(Long eventId, String message) {
        final var entity = outboxRepository.selectForUpdateById(eventId)
                .orElseThrow(() -> internalServerError("Event %d not found".formatted(eventId)));

        entity.setStatus(EventEntity.Status.SKIPPED);
        entity.setError(message);
        outboxRepository.saveAndFlush(entity);
    }
}
