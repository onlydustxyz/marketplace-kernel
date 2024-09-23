package onlydust.com.marketplace.kernel.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@NoArgsConstructor(staticName = "random")
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class EcosystemId extends UuidWrapper {
    public static EcosystemId of(@NonNull final UUID uuid) {
        return EcosystemId.builder().uuid(uuid).build();
    }

    @JsonCreator
    public static EcosystemId of(@NonNull final String uuid) {
        return EcosystemId.of(UUID.fromString(uuid));
    }
}