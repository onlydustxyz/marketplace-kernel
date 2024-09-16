package onlydust.com.marketplace.kernel.model.blockchain;


import onlydust.com.marketplace.kernel.model.blockchain.evm.EvmAccountAddress;
import onlydust.com.marketplace.kernel.model.blockchain.evm.EvmContractAddress;
import onlydust.com.marketplace.kernel.model.blockchain.evm.EvmTransaction;

public interface Optimism {
    static EvmTransaction.Hash transactionHash(String value) {
        return new EvmTransaction.Hash(value);
    }

    static EvmAccountAddress accountAddress(String value) {
        return new EvmAccountAddress(value);
    }

    static EvmContractAddress contractAddress(String address) {
        return new EvmContractAddress(address);
    }
}
