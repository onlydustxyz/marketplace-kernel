package onlydust.com.marketplace.kernel.model.blockchain.stellar;

import lombok.NonNull;
import onlydust.com.marketplace.kernel.model.blockchain.Base32Hash;

public class StellarAccountId extends Base32Hash {
    private static final int MAX_SIZE = 56;

    private StellarAccountId(final @NonNull String accountId) {
        super(MAX_SIZE, accountId);
    }

    public static StellarAccountId of(final @NonNull String accountId) {
        return new StellarAccountId(accountId);
    }
}
