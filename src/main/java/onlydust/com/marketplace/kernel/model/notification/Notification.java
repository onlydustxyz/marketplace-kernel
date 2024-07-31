package onlydust.com.marketplace.kernel.model.notification;

import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import onlydust.com.marketplace.kernel.model.UuidWrapper;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@EqualsAndHashCode
@ToString
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@Accessors(fluent = true)
public class Notification {
    @NonNull Id id;

    @NonNull UUID recipientId;

    @NonNull NotificationData data;

    @NonNull ZonedDateTime createdAt;

    @NonNull Set<NotificationChannel> channels;

    public static Notification of(@NonNull UUID recipientId,
                                  @NonNull NotificationData notificationData,
                                  @NonNull Set<NotificationChannel> channels) {
        return Notification.builder()
                .id(Id.random())
                .recipientId(recipientId)
                .data(notificationData)
                .createdAt(ZonedDateTime.now(ZoneOffset.UTC))
                .channels(channels)
                .build();
    }

    @NoArgsConstructor(staticName = "random")
    @EqualsAndHashCode(callSuper = true)
    @SuperBuilder
    public static class Id extends UuidWrapper {
        public static Id of(@NonNull final UUID uuid) {
            return Id.builder().uuid(uuid).build();
        }

        public static Id of(@NonNull final String uuid) {
            return Id.of(UUID.fromString(uuid));
        }
    }
}
