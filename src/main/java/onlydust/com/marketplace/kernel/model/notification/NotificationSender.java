package onlydust.com.marketplace.kernel.model.notification;

/**
 * Implementations typically send pending notification of specific channel(s).
 * Example: EmailNotificationSender, SmsNotificationSender, etc.
 */
public interface NotificationSender {
    void sendAll();
}
