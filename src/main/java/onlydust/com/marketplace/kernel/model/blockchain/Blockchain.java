package onlydust.com.marketplace.kernel.model.blockchain;

import java.math.BigDecimal;
import java.net.URI;
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
        Blockchain blockchain();

        Status status();

        String reference();

        ZonedDateTime timestamp();

        enum Status {
            PENDING, CONFIRMED, FAILED
        }
    }

    public interface TransferTransaction extends Transaction {
        String senderAddress();

        String recipientAddress();

        BigDecimal amount();

        Optional<String> contractAddress();
    }

    public URI getBlockExplorerUrl(final String reference) {
        return switch (this) {
            case ETHEREUM -> Ethereum.BLOCK_EXPLORER.url(Ethereum.transactionHash(reference));
            case OPTIMISM -> Optimism.BLOCK_EXPLORER.url(Optimism.transactionHash(reference));
            case STARKNET -> StarkNet.BLOCK_EXPLORER.url(StarkNet.transactionHash(reference));
            case APTOS -> Aptos.BLOCK_EXPLORER.url(Aptos.transactionHash(reference));
            case STELLAR -> Stellar.BLOCK_EXPLORER.url(Stellar.transactionHash(reference));
        };
    }
}
