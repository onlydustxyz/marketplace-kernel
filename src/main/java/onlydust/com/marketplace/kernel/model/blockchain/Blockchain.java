package onlydust.com.marketplace.kernel.model.blockchain;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Optional;

public enum Blockchain {
    ETHEREUM, OPTIMISM, STARKNET, APTOS, STELLAR;

    public String pretty() {
        return switch (this) {
            case ETHEREUM -> "Ethereum";
            case OPTIMISM -> "Optimism";
            case STARKNET -> "StarkNet";
            case APTOS -> "Aptos";
            case STELLAR -> "Stellar";
        };
    }

    public interface Transaction {
        String reference();

        ZonedDateTime timestamp();
    }

    public interface TransferTransaction extends Transaction {
        String senderAddress();

        String recipientAddress();

        BigDecimal amount();

        Optional<String> contractAddress();
    }
}
