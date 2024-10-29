package onlydust.com.marketplace.kernel.model.blockchain;

import lombok.EqualsAndHashCode;
import lombok.NonNull;

import java.util.regex.Pattern;

import static onlydust.com.marketplace.kernel.exception.OnlyDustException.badRequest;

@EqualsAndHashCode(callSuper = true)
public abstract class Base58Hash extends Hash {
    private static final Pattern BASE58_PATTERN = Pattern.compile("[123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz]+");

    protected Base58Hash(final int maxSize, final @NonNull String hash) {
        super(maxSize, hash);

        if (!BASE58_PATTERN.matcher(hash).matches())
            throw badRequest("Provided hash is not base 58");
    }
}
