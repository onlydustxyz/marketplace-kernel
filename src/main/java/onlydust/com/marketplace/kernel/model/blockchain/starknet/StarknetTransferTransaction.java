package onlydust.com.marketplace.kernel.model.blockchain.starknet;

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
public class StarknetTransferTransaction extends StarknetTransaction implements TransferTransaction {
    @NonNull
    StarknetAccountAddress sender;
    @NonNull
    StarknetAccountAddress recipient;
    @NonNull
    BigDecimal amount;
    StarknetContractAddress contractAddress;

    public StarknetTransferTransaction(final @NonNull Hash hash, final @NonNull ZonedDateTime timestamp, final @NonNull Status status, final @NonNull StarknetAccountAddress sender, final @NonNull StarknetAccountAddress recipient, final @NonNull BigDecimal amount, final StarknetContractAddress contractAddress) {
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
        return Optional.ofNullable(contractAddress).map(StarknetContractAddress::toString);
    }
}
