package onlydust.com.marketplace.kernel.model.blockchain;

import lombok.EqualsAndHashCode;
import lombok.NonNull;

import java.util.regex.Pattern;

import static onlydust.com.marketplace.kernel.exception.OnlyDustException.badRequest;

@EqualsAndHashCode(callSuper = true)
public abstract class Base32Hash extends Hash {
    private static final Pattern BASE32_PATTERN = Pattern.compile("[A-Z2-7]+");

    protected Base32Hash(final int maxSize, final @NonNull String hash) {
        super(maxSize, hash);

        if (!BASE32_PATTERN.matcher(hash).matches())
            throw badRequest("Provided hash is not base 32");
    }
}
