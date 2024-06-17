package onlydust.com.marketplace.kernel.model.event;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.Accessors;
import onlydust.com.marketplace.kernel.model.Event;
import onlydust.com.marketplace.kernel.model.EventType;

@Value
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(fluent = true)
@EventType("OnGithubIssueDeleted")
@NoArgsConstructor(force = true)
@AllArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OnGithubIssueDeleted extends Event {
    @NonNull
    Long id;
}
