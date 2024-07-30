package onlydust.com.marketplace.kernel.model.notification;

import java.util.List;

public enum NotificationCategory {
    /** Notify maintainers about their committee applications (eg. created, accepted, etc.) */
    COMMITTEE_APPLICATION_AS_MAINTAINER,
    /** Notify contributors about their rewards (eg. received, paid, etc.) */
    REWARD_AS_CONTRIBUTOR,
    /** Notify contributors about their project applications (eg. accepted, etc.) */
    PROJECT_APPLICATION_AS_CONTRIBUTOR,
    /** Notify maintainers about their project applications (eg. contributor applied, etc.) */
    PROJECT_APPLICATION_AS_MAINTAINER,
    /** Notify contributors about specific projects good-first-issues (eg. new one added, etc.) */
    PROJECT_GOOD_FIRST_ISSUE_AS_CONTRIBUTOR;

    public List<NotificationChannel> defaultChannels() {
        return List.of(NotificationChannel.IN_APP, NotificationChannel.EMAIL);
    }
}
