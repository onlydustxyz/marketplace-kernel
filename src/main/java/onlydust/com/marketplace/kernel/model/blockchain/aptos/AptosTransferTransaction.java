package onlydust.com.marketplace.kernel.model.blockchain.aptos;

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
public class AptosTransferTransaction extends AptosTransaction implements TransferTransaction {
    @NonNull
    AptosAccountAddress sender;
    @NonNull
    AptosAccountAddress recipient;
    @NonNull
    BigDecimal amount;
    AptosCoinType coinType;

    public AptosTransferTransaction(final @NonNull AptosTransaction.Hash hash, final @NonNull ZonedDateTime timestamp, final @NonNull Status status, final @NonNull AptosAccountAddress sender, final @NonNull AptosAccountAddress recipient, final @NonNull BigDecimal amount, final AptosCoinType coinType) {
        super(hash, timestamp, status);

        this.sender = sender;
        this.recipient = recipient;
        this.amount = amount;
        this.coinType = coinType;
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
        return Optional.ofNullable(coinType).map(AptosCoinType::toString);
    }
}
