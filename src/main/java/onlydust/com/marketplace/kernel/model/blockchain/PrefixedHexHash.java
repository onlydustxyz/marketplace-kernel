package onlydust.com.marketplace.kernel.model.blockchain;

import lombok.EqualsAndHashCode;
import lombok.NonNull;

import static java.lang.Math.min;
import static onlydust.com.marketplace.kernel.exception.OnlyDustException.badRequest;

@EqualsAndHashCode(callSuper = true)
public abstract class PrefixedHexHash extends HexHash {
    private final String prefix;

    protected PrefixedHexHash(final int maxByteCount, final @NonNull String hash) {
        this(maxByteCount, "0x", hash);
    }

    protected PrefixedHexHash(final int maxByteCount, final @NonNull String prefix, final @NonNull String hash) {
        super(maxByteCount, hash.substring(min(hash.length(), prefix.length())));
        this.prefix = prefix;

        final var hashPrefix = hash.substring(0, prefix.length());
        if (!hashPrefix.equalsIgnoreCase(prefix))
            throw badRequest("Provided hash should start with %s".formatted(prefix));
    }

    @Override
    public String toString() {
        return prefix + super.toString();
    }
}
