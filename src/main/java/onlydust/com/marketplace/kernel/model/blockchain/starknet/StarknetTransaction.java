package onlydust.com.marketplace.kernel.model.blockchain.starknet;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import onlydust.com.marketplace.kernel.model.blockchain.Blockchain;
import onlydust.com.marketplace.kernel.model.blockchain.Blockchain.Transaction;
import onlydust.com.marketplace.kernel.model.blockchain.PrefixedHexHash;

import java.time.ZonedDateTime;

public record StarknetTransaction(Hash hash, ZonedDateTime timestamp, Status status) implements Transaction {

    @Override
    public Blockchain blockchain() {
        return Blockchain.STARKNET;
    }

    @Override
    public String reference() {
        return hash().toString();
    }

    @EqualsAndHashCode(callSuper = true)
    public static class Hash extends PrefixedHexHash {
        private static final int MAX_BYTE_COUNT = 32;

        public Hash(final @NonNull String address) {
            super(MAX_BYTE_COUNT, address);
        }
    }
}
