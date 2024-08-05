package onlydust.com.marketplace.kernel.model.blockchain.stellar;

import lombok.NonNull;
import onlydust.com.marketplace.kernel.model.blockchain.Base32Hash;

public class StellarContractAddress extends Base32Hash {
    private static final int MAX_SIZE = 56;

    private StellarContractAddress(final @NonNull String accountId) {
        super(MAX_SIZE, accountId);
    }

    public static StellarContractAddress of(final @NonNull String accountId) {
        return new StellarContractAddress(accountId);
    }
}
