package onlydust.com.marketplace.kernel.model.blockchain.stellar;

import onlydust.com.marketplace.kernel.model.blockchain.BlockExplorer;

import java.net.URI;

public class StellarExpert implements BlockExplorer<StellarTransaction.Hash> {
    private static final String BASE_URL = "https://stellar.expert/explorer/public";

    @Override
    public URI url(StellarTransaction.Hash transactionHash) {
        return URI.create(BASE_URL + "/tx/" + transactionHash);
    }
}
