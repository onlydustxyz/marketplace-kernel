package onlydust.com.marketplace.kernel.model.blockchain;

import lombok.EqualsAndHashCode;
import lombok.NonNull;

import java.util.regex.Pattern;

import static onlydust.com.marketplace.kernel.exception.OnlyDustException.badRequest;

@EqualsAndHashCode(callSuper = true)
public abstract class HexHash extends Hash {
    private static final Pattern HEX_PATTERN = Pattern.compile("[0-9a-fA-F]+");

    protected HexHash(final int maxByteCount, final @NonNull String hash) {
        this(maxByteCount, hash, true);
    }
    
    protected HexHash(final int maxByteCount, final @NonNull String hash, boolean sanitized) {
        super(maxByteCount * 2, sanitized ? sanitize(hash) : hash);

        if (!HEX_PATTERN.matcher(hash).matches())
            throw badRequest("Provided hash is not hexadecimal");
    }

    private static String sanitize(final @NonNull String hash) {
        return (hash.length() % 2 == 0 ? "" : "0") + hash;
    }
}
