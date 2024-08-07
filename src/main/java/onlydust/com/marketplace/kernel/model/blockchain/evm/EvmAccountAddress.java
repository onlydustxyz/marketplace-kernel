package onlydust.com.marketplace.kernel.model.blockchain.evm;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import onlydust.com.marketplace.kernel.model.blockchain.PrefixedHexHash;

@EqualsAndHashCode(callSuper = true)
public class EvmAccountAddress extends PrefixedHexHash {
    private static final int MAX_BYTE_COUNT = 20;

    public EvmAccountAddress(final @NonNull String address) {
        super(MAX_BYTE_COUNT, address);
    }
}
