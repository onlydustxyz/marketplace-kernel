package onlydust.com.marketplace.kernel.port.output;

import onlydust.com.marketplace.kernel.model.ProgramId;
import onlydust.com.marketplace.kernel.model.ProjectId;
import onlydust.com.marketplace.kernel.model.SponsorId;
import onlydust.com.marketplace.kernel.model.UserId;

public interface PermissionPort {
    boolean isUserProjectLead(ProjectId projectId, UserId projectLeadId);

    boolean isUserContributor(String contributionId, Long githubUserId);

    boolean isRepoLinkedToProject(ProjectId projectId, Long githubRepoId);

    boolean hasUserAccessToProject(ProjectId projectId, UserId userId);

    boolean hasUserAccessToProject(String projectSlug, UserId userId);

    boolean isUserSponsorLead(UserId userId, SponsorId sponsorId);

    boolean isUserSponsorLeadOfProgram(UserId userId, ProgramId programId);

    boolean isUserProgramLead(UserId userId, ProgramId programId);
}