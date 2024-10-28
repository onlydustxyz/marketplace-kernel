package onlydust.com.marketplace.kernel.model.blockchain.near;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import onlydust.com.marketplace.kernel.model.blockchain.Blockchain.TransferTransaction;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Optional;

@EqualsAndHashCode(callSuper = true)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Getter
@Accessors(fluent = true)
public class NearTransferTransaction extends NearTransaction implements TransferTransaction {
    @NonNull
    String sender;
    @NonNull
    String recipient;
    @NonNull
    BigDecimal amount;
    String contractAddress;

    public NearTransferTransaction(final @NonNull Hash hash, final @NonNull ZonedDateTime timestamp, final @NonNull Status status, final @NonNull String sender, final @NonNull String recipient, final @NonNull BigDecimal amount, final String contractAddress) {
        super(hash, timestamp, status);

        this.sender = sender;
        this.recipient = recipient;
        this.amount = amount;
        this.contractAddress = contractAddress;
    }

    @Override
    public String senderAddress() {
        return sender;
    }

    @Override
    public String recipientAddress() {
        return recipient;
    }

    @Override
    public Optional<String> contractAddress() {
        return Optional.ofNullable(contractAddress).map(String::toString);
    }
}
