package onlydust.com.marketplace.kernel.model.blockchain.near;

import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import onlydust.com.marketplace.kernel.model.blockchain.Base58Hash;
import onlydust.com.marketplace.kernel.model.blockchain.Blockchain;
import onlydust.com.marketplace.kernel.model.blockchain.Blockchain.Transaction;

import java.time.ZonedDateTime;

@AllArgsConstructor
@EqualsAndHashCode
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Getter
@Accessors(fluent = true)
public class NearTransaction implements Transaction {
    Hash hash;
    ZonedDateTime timestamp;
    Status status;

    @Override
    public Blockchain blockchain() {
        return Blockchain.NEAR;
    }

    @Override
    public String reference() {
        return hash().toString();
    }

    @EqualsAndHashCode(callSuper = true)
    public static class Hash extends Base58Hash {
        private static final int MAX_BYTE_COUNT = 44;

        public Hash(final @NonNull String address) {
            super(MAX_BYTE_COUNT, address);
        }
    }
}