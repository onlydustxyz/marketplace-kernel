package onlydust.com.marketplace.kernel.port.output;

import onlydust.com.marketplace.kernel.model.notification.Notification;
import onlydust.com.marketplace.kernel.model.notification.NotificationData;

import java.util.UUID;

public interface NotificationPort {
    /**
     * Pushes a notification to a recipient.
     *
     * @param recipientId recipient's user ID
     * @param notificationData notification to push
     * @return the pushed notification
     */
    Notification push(UUID recipientId, NotificationData notificationData);

    //void markAsSent(NotificationChannel channel, List<UUID> notificationId);
}
