package onlydust.com.marketplace.kernel.model.notification;

import lombok.Data;


@Data
public abstract class Notification {
    public abstract NotificationCategory category();
}
