package onlydust.com.marketplace.kernel.model.blockchain.aptos;

import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import onlydust.com.marketplace.kernel.model.blockchain.Blockchain.Transaction;
import onlydust.com.marketplace.kernel.model.blockchain.PrefixedHexHash;

import java.time.ZonedDateTime;

@AllArgsConstructor
@EqualsAndHashCode
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Getter
@Accessors(fluent = true)
public class AptosTransaction implements Transaction {
    Hash hash;
    
    ZonedDateTime timestamp;

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
