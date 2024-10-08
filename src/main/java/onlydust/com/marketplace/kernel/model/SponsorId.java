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
public class SponsorId extends UuidWrapper {
    public static SponsorId of(@NonNull final UUID uuid) {
        return SponsorId.builder().uuid(uuid).build();
    }

    @JsonCreator
    public static SponsorId of(@NonNull final String uuid) {
        return SponsorId.of(UUID.fromString(uuid));
    }

    public String pretty() {
        return "#" + this.value().toString().substring(0, 5).toUpperCase();
    }
}