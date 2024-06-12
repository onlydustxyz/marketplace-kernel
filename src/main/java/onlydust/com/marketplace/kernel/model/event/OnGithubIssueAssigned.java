package onlydust.com.marketplace.kernel.model.event;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.*;
import lombok.experimental.Accessors;
import onlydust.com.marketplace.kernel.model.Event;
import onlydust.com.marketplace.kernel.model.EventType;

import java.time.ZonedDateTime;
import java.util.Set;

@Value
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(fluent = true)
@EventType("OnGithubIssueAssigned")
@NoArgsConstructor(force = true)
@AllArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class OnGithubIssueAssigned extends Event {
    @NonNull
    Long id;
    @NonNull
    Long assigneeId;
    @NonNull
    ZonedDateTime assignedAt;
    @NonNull
    Set<String> labels;
}
