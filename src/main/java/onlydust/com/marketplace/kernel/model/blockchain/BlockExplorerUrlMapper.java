package onlydust.com.marketplace.kernel.model.blockchain;

import onlydust.com.marketplace.kernel.exception.OnlyDustException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.of;

public interface BlockExplorerUrlMapper {

    Logger LOGGER = LoggerFactory.getLogger(BlockExplorerUrlMapper.class);

    static Optional<URI> getBlockExplorerUrl(final Blockchain blockchain, final String reference) {
        try {
            return switch (blockchain) {
                case ETHEREUM -> of(Ethereum.BLOCK_EXPLORER.url(Ethereum.transactionHash(reference)));
                case OPTIMISM -> of(Optimism.BLOCK_EXPLORER.url(Optimism.transactionHash(reference)));
                case STARKNET -> of(StarkNet.BLOCK_EXPLORER.url(StarkNet.transactionHash(reference)));
                case APTOS -> of(Aptos.BLOCK_EXPLORER.url(Aptos.transactionHash(reference)));
                case STELLAR -> of(Stellar.BLOCK_EXPLORER.url(Stellar.transactionHash(reference)));
            };
        } catch (OnlyDustException e) {
            LOGGER.error("Error while generating block explorer URL for blockchain %s and reference %s".formatted(blockchain, reference), e);
            return empty();
        }
    }
}
