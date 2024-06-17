package onlydust.com.marketplace.kernel.model.event;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.Accessors;
import onlydust.com.marketplace.kernel.model.Event;
import onlydust.com.marketplace.kernel.model.EventType;

import java.time.ZonedDateTime;

@Value
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(fluent = true)
@EventType("OnGithubCommentEdited")
@NoArgsConstructor(force = true)
@AllArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OnGithubCommentEdited extends Event {
    @NonNull
    Long id;
    @NonNull
    Long issueId;
    @NonNull
    Long repoId;
    @NonNull
    Long authorId;
    @NonNull
    ZonedDateTime updatedAt;
    @NonNull
    String body;
}
