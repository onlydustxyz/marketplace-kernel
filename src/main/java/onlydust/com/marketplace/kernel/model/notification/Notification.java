package onlydust.com.marketplace.kernel.model.notification;

import lombok.*;
import lombok.experimental.Accessors;

import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;

@Value
@Builder
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Accessors(fluent = true)
public class Notification {
    @EqualsAndHashCode.Include
    @NonNull
    UUID id;

    @NonNull UUID recipientId;

    @NonNull NotificationData data;

    @NonNull ZonedDateTime createdAt;

    @NonNull Set<NotificationChannel> channels;

    public static Notification of(@NonNull UUID recipientId,
                                  @NonNull NotificationData notificationData,
                                  @NonNull Set<NotificationChannel> channels) {
        return Notification.builder()
                .id(UUID.randomUUID())
                .recipientId(recipientId)
                .data(notificationData)
                .createdAt(ZonedDateTime.now())
                .channels(channels)
                .build();
    }
}
