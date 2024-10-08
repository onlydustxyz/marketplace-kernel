package onlydust.com.marketplace.kernel.model.blockchain;


import onlydust.com.marketplace.kernel.model.blockchain.aptos.AptosAccountAddress;
import onlydust.com.marketplace.kernel.model.blockchain.aptos.AptosCoinType;
import onlydust.com.marketplace.kernel.model.blockchain.aptos.AptosTransaction;

public interface Aptos {
    static AptosTransaction.Hash transactionHash(String value) {
        return new AptosTransaction.Hash(value);
    }

    static AptosAccountAddress accountAddress(String value) {
        return new AptosAccountAddress(value);
    }

    static AptosCoinType coinType(String address) {
        return new AptosCoinType(address);
    }
}
