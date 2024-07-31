package onlydust.com.marketplace.kernel.model.github;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Accessors(fluent = true)
public class GithubUserIdentity {
    @EqualsAndHashCode.Include
    Long githubUserId;
    String login;
    String avatarUrl;
    String email;
}
