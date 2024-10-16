package onlydust.com.marketplace.kernel.port.output;

import onlydust.com.marketplace.kernel.model.*;

import java.util.List;
import java.util.Optional;

public interface PermissionPort {
    boolean isUserProjectLead(OrSlug<ProjectId> projectIdOrSlug, UserId projectLeadId);

    boolean isUserProjectLead(ProjectId projectId, UserId projectLeadId);

    boolean isUserContributor(String contributionId, Long githubUserId);

    boolean isRepoLinkedToProject(ProjectId projectId, Long githubRepoId);

    boolean hasUserAccessToProject(ProjectId projectId, Optional<UserId> userId);

    boolean hasUserAccessToProject(String projectSlug, Optional<UserId> userId);

    boolean isUserSponsorLead(UserId userId, SponsorId sponsorId);

    boolean isUserSponsorLeadOfProgram(UserId userId, ProgramId programId);

    boolean isUserProgramLead(UserId userId, ProgramId programId);

    List<ProgramId> getLedProgramIds(UserId userId);

    List<EcosystemId> getLedEcosystemIds(UserId userId);

    List<SponsorId> getLedSponsorIds(UserId userId);

    List<ProjectId> getLedProjectIds(UserId userId);

    default boolean hasUserAccessToProgram(UserId userId, ProgramId programId) {
        return isUserProgramLead(userId, programId) || isUserSponsorLeadOfProgram(userId, programId);
    }

    boolean canUserUpdateIssue(UserId userId, Long githubIssueId);

    boolean canUserUpdatePullRequest(UserId userId, Long githubPullRequestId);
}