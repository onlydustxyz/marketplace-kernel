package onlydust.com.marketplace.kernel.model.notification;

import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.Accessors;

import java.util.UUID;

/**
 * Represents the recipient (user) of a notification.
 */
@Value
@Accessors(fluent = true, chain = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class NotificationRecipient {
    @EqualsAndHashCode.Include UUID userId;
    String email;
    String login;
}
