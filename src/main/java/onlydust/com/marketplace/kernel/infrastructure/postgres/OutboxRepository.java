package onlydust.com.marketplace.kernel.infrastructure.postgres;

import jakarta.persistence.LockModeType;
import lombok.NonNull;
import onlydust.com.marketplace.kernel.model.Event;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

@NoRepositoryBean
public interface OutboxRepository<E extends EventEntity> extends Repository<E, Long> {

    void saveEvent(@NonNull Event event);

    @Query(value = """
            SELECT next_event
            FROM #{#entityName} next_event
            WHERE next_event.id = :id
            """)
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @NonNull
    Optional<E> selectForUpdateById(@Param("id") @NonNull Long id);

    @Query(value = """
            SELECT next_event
            FROM #{#entityName} next_event
            WHERE next_event.status IN ('PENDING', 'FAILED')
            ORDER BY next_event.id ASC
            LIMIT 1
            """)
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @NonNull
    Optional<E> findNextToProcess();
}
