package onlydust.com.marketplace.kernel.model.blockchain;

import onlydust.com.marketplace.kernel.model.blockchain.stellar.StellarAccountId;
import onlydust.com.marketplace.kernel.model.blockchain.stellar.StellarContractAddress;
import onlydust.com.marketplace.kernel.model.blockchain.stellar.StellarTransaction;

public interface Stellar {
    static StellarTransaction.Hash transactionHash(String value) {
        return new StellarTransaction.Hash(value);
    }

    static StellarAccountId accountId(String value) {
        return StellarAccountId.of(value);
    }

    static StellarContractAddress contractAddress(final String address) {
        return StellarContractAddress.of(address);
    }
}
