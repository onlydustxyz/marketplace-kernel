package onlydust.com.marketplace.kernel.port.output;

import onlydust.com.marketplace.kernel.model.UserId;
import onlydust.com.marketplace.kernel.model.notification.Notification;
import onlydust.com.marketplace.kernel.model.notification.NotificationData;

public interface NotificationPort {
    /**
     * Pushes a notification to a recipient.
     *
     * @param recipientId      recipient's user ID
     * @param notificationData notification to push
     * @return the pushed notification
     */
    Notification push(UserId recipientId, NotificationData notificationData);
}
