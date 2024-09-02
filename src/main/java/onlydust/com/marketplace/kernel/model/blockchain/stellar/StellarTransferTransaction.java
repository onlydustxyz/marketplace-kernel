package onlydust.com.marketplace.kernel.model.blockchain.stellar;

import lombok.*;
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
@ToString
public class StellarTransferTransaction extends StellarTransaction implements TransferTransaction {
    @NonNull
    StellarAccountId sender;
    @NonNull
    StellarAccountId recipient;
    @NonNull
    BigDecimal amount;
    StellarContractAddress contractAddress;

    public StellarTransferTransaction(final @NonNull Hash hash, final @NonNull ZonedDateTime timestamp, final @NonNull Status status, final @NonNull StellarAccountId sender, final @NonNull StellarAccountId recipient, final @NonNull BigDecimal amount, final StellarContractAddress contractAddress) {
        super(hash, timestamp, status);

        this.sender = sender;
        this.recipient = recipient;
        this.amount = amount;
        this.contractAddress = contractAddress;
    }

    @Override
    public String senderAddress() {
        return sender.toString();
    }

    @Override
    public String recipientAddress() {
        return recipient.toString();
    }

    @Override
    public Optional<String> contractAddress() {
        return Optional.ofNullable(contractAddress).map(StellarContractAddress::toString);
    }
}
