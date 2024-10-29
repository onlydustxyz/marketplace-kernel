package onlydust.com.marketplace.kernel.model.blockchain;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import onlydust.com.marketplace.kernel.model.blockchain.aptos.AptosTransaction;
import onlydust.com.marketplace.kernel.model.blockchain.evm.EvmTransaction;
import onlydust.com.marketplace.kernel.model.blockchain.near.NearTransaction;
import onlydust.com.marketplace.kernel.model.blockchain.starknet.StarknetTransaction;
import onlydust.com.marketplace.kernel.model.blockchain.stellar.StellarTransaction;

import java.net.URI;

@AllArgsConstructor
public class MetaBlockExplorer {
    private final BlockExplorer<AptosTransaction.Hash> aptosBlockExplorer;
    private final BlockExplorer<EvmTransaction.Hash> ethereumBlockExplorer;
    private final BlockExplorer<EvmTransaction.Hash> optimismBlockExplorer;
    private final BlockExplorer<StarknetTransaction.Hash> starknetBlockExplorer;
    private final BlockExplorer<StellarTransaction.Hash> stellarBlockExplorer;
    private final BlockExplorer<NearTransaction.Hash> nearBlockExplorer;

    public URI url(final @NonNull Blockchain blockchain, final @NonNull String reference) {
        return switch (blockchain) {
            case APTOS -> aptosBlockExplorer.url(Aptos.transactionHash(reference));
            case ETHEREUM -> ethereumBlockExplorer.url(Ethereum.transactionHash(reference));
            case OPTIMISM -> optimismBlockExplorer.url(Ethereum.transactionHash(reference));
            case STARKNET -> starknetBlockExplorer.url(StarkNet.transactionHash(reference));
            case STELLAR -> stellarBlockExplorer.url(Stellar.transactionHash(reference));
            case NEAR -> nearBlockExplorer.url(Near.transactionHash(reference));
        };
    }

    public URI url(final @NonNull Blockchain.Transaction transaction) {
        return url(transaction.blockchain(), transaction.reference());
    }
}
