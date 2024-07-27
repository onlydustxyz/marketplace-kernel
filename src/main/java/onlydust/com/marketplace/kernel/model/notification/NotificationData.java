package onlydust.com.marketplace.kernel.model.notification;

import lombok.Data;


@Data
public abstract class NotificationData {
    public abstract NotificationCategory category();
}
