package onlydust.com.marketplace.kernel.port.output;

import onlydust.com.marketplace.kernel.model.notification.Notification;
import onlydust.com.marketplace.kernel.model.notification.NotificationChannel;
import onlydust.com.marketplace.kernel.model.notification.NotificationData;
import onlydust.com.marketplace.kernel.model.notification.NotificationRecipient;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface NotificationPort {
    /**
     * Pushes a notification to a recipient.
     *
     * @param recipientId recipient's user ID
     * @param notificationData notification to push
     */
    void push(UUID recipientId, NotificationData notificationData);

    /**
     * Retrieves all pending notifications per recipient for a given channel.
     *
     * @param channel notification channel
     * @return a map of recipients and their pending notifications that must be sent through the given channel
     */
    Map<NotificationRecipient, List<Notification>> getPendingNotificationsPerRecipient(NotificationChannel channel);
}
