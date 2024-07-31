package onlydust.com.marketplace.kernel.model;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class AuthenticatedUserTest {

    @Test
    void should_return_company_billing_profile_where_i_am_admin() {
        // Given
        final var billingProfiles = List.of(
                new AuthenticatedUser.BillingProfileMembership(UUID.randomUUID(), AuthenticatedUser.BillingProfileMembership.Role.ADMIN),
                new AuthenticatedUser.BillingProfileMembership(UUID.randomUUID(), AuthenticatedUser.BillingProfileMembership.Role.ADMIN),
                new AuthenticatedUser.BillingProfileMembership(UUID.randomUUID(), AuthenticatedUser.BillingProfileMembership.Role.ADMIN),
                new AuthenticatedUser.BillingProfileMembership(UUID.randomUUID(), AuthenticatedUser.BillingProfileMembership.Role.MEMBER)
        );
        final AuthenticatedUser user = AuthenticatedUser.builder()
                .billingProfiles(billingProfiles)
                .build();

        // When
        final var administratedBillingProfiles = user.administratedBillingProfiles();

        // Then
        assertThat(administratedBillingProfiles).hasSize(3);
        assertThat(administratedBillingProfiles).doesNotContain(billingProfiles.get(3).billingProfileId());
    }

}