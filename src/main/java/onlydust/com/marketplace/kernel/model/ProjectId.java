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
public class ProjectId extends UuidWrapper {
    public static ProjectId of(@NonNull final UUID uuid) {
        return ProjectId.builder().uuid(uuid).build();
    }

    @JsonCreator
    public static ProjectId of(@NonNull final String uuid) {
        return ProjectId.of(UUID.fromString(uuid));
    }
}