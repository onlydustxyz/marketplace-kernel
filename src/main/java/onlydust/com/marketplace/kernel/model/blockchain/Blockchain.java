package onlydust.com.marketplace.kernel.model.blockchain;

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
}
