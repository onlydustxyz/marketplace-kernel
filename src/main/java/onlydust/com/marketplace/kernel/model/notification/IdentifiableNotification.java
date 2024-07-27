package onlydust.com.marketplace.kernel.model.notification;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;
import lombok.experimental.Accessors;

import java.time.ZonedDateTime;
import java.util.UUID;

@Value
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Accessors(fluent = true)
public class IdentifiableNotification {
    @EqualsAndHashCode.Include
    @NonNull
    UUID id;

    @NonNull NotificationData data;

    @NonNull ZonedDateTime createdAt;
}
