package onlydust.com.marketplace.kernel.model.blockchain;


import onlydust.com.marketplace.kernel.model.blockchain.near.NearTransaction;

public interface Near {
    static NearTransaction.Hash transactionHash(String value) {
        return new NearTransaction.Hash(value);
    }

}
