package onlydust.com.marketplace.kernel.model;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@NoArgsConstructor(staticName = "random")
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class UserId extends UuidWrapper {
    public static UserId of(@NonNull final UUID uuid) {
        return UserId.builder().uuid(uuid).build();
    }

    public static UserId of(@NonNull final String uuid) {
        return UserId.of(UUID.fromString(uuid));
    }
}