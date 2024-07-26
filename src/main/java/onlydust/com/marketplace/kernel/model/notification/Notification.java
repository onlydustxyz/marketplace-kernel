package onlydust.com.marketplace.kernel.model.notification;

import lombok.EqualsAndHashCode;

import java.time.ZonedDateTime;


@EqualsAndHashCode
public abstract class Notification {
    protected final ZonedDateTime createdAt = ZonedDateTime.now();

    public abstract NotificationCategory category();

    public ZonedDateTime createdAt() {
        return createdAt;
    }
}
