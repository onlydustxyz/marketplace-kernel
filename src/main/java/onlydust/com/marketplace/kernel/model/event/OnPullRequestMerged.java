package onlydust.com.marketplace.kernel.model.event;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.*;
import lombok.experimental.Accessors;
import onlydust.com.marketplace.kernel.model.Event;
import onlydust.com.marketplace.kernel.model.EventType;

import java.time.ZonedDateTime;

@Value
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(fluent = true)
@EventType("OnPullRequestMerged")
@NoArgsConstructor(force = true)
@AllArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class OnPullRequestMerged extends Event {
    @NonNull
    Long id;
    @NonNull
    Long authorId;
    @NonNull
    ZonedDateTime createdAt;
    @NonNull
    ZonedDateTime mergedAt;
}