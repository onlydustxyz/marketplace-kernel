package onlydust.com.marketplace.kernel.model.blockchain;

import lombok.EqualsAndHashCode;
import lombok.NonNull;

import static onlydust.com.marketplace.kernel.exception.OnlyDustException.badRequest;

@EqualsAndHashCode
public abstract class Hash {
    private final String inner;

    protected Hash(final int maxSize, final @NonNull String hash) {
        if (hash.isEmpty())
            throw badRequest("Provided hash should not be empty");

        if (hash.length() > maxSize)
            throw badRequest("Provided hash should be less than %d characters".formatted(maxSize));

        this.inner = hash;
    }

    @Override
    public String toString() {
        return inner;
    }
}
