package onlydust.com.marketplace.kernel.model.notification;

import java.util.List;

public enum NotificationCategory {
    /** E.g. application to review, etc. */
    MAINTAINER_PROJECT_CONTRIBUTOR,
    /** E.g. committee application created, etc. */
    MAINTAINER_PROJECT_PROGRAM,
    /** E.g. invoice rejected, reward received/canceled/paid, etc. */
    CONTRIBUTOR_REWARD,
    /** E.g. issue application rejected/accepted, new good first issue in project X, etc. */
    CONTRIBUTOR_PROJECT,
    /** E.g. KYCB document failed, KYCB complete your profile, etc.  */
    KYC_KYB_BILLING_PROFILE,
    ;

    public List<NotificationChannel> defaultChannels() {
        return List.of(NotificationChannel.IN_APP, NotificationChannel.EMAIL);
    }
}
