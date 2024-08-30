package onlydust.com.marketplace.kernel.model.blockchain.evm;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import onlydust.com.marketplace.kernel.model.blockchain.Blockchain;
import onlydust.com.marketplace.kernel.model.blockchain.Blockchain.TransferTransaction;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Optional;

@EqualsAndHashCode(callSuper = true)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Getter
@Accessors(fluent = true)
public class EvmTransferTransaction extends EvmTransaction implements TransferTransaction {
    @NonNull
    EvmAccountAddress sender;
    @NonNull
    EvmAccountAddress recipient;
    @NonNull
    BigDecimal amount;
    EvmContractAddress contractAddress;

    public EvmTransferTransaction(final @NonNull Blockchain blockchain, final @NonNull Hash hash, final @NonNull ZonedDateTime timestamp, final @NonNull Status status, final @NonNull EvmAccountAddress sender, final @NonNull EvmAccountAddress recipient, final @NonNull BigDecimal amount, EvmContractAddress contractAddress) {
        super(blockchain, hash, timestamp, status);

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
        return Optional.ofNullable(contractAddress).map(EvmContractAddress::toString);
    }
}
