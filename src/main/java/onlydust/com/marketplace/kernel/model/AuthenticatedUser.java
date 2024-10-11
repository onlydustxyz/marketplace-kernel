package onlydust.com.marketplace.kernel.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import onlydust.com.marketplace.kernel.model.github.GithubUserIdentity;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Value
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
@Accessors(fluent = true)
public class AuthenticatedUser extends GithubUserIdentity {
    UserId id;
    ZonedDateTime createdAt;
    ZonedDateTime lastSeenAt;
    @Builder.Default
    List<Role> roles = new ArrayList<>();
    @Builder.Default
    List<UUID> projectsLed = new ArrayList<>();
    @Builder.Default
    List<BillingProfileMembership> billingProfiles = new ArrayList<>();

    public List<UUID> administratedBillingProfiles() {
        return billingProfiles.stream().filter(bpm -> bpm.role() == BillingProfileMembership.Role.ADMIN).map(BillingProfileMembership::billingProfileId).toList();
    }

    public enum Role {
        ADMIN, USER, INTERNAL_SERVICE, UNSAFE_INTERNAL_SERVICE
    }

    public record BillingProfileMembership(UUID billingProfileId, Role role) {
        public enum Role {
            ADMIN, MEMBER;
        }
    }
}
