package onlydust.com.marketplace.kernel.model.blockchain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

import java.util.regex.Pattern;

import static onlydust.com.marketplace.kernel.exception.OnlyDustException.badRequest;

public abstract class Hash {
    public static final Pattern HEX = Pattern.compile("[0-9a-fA-F]+");

    final @NonNull String inner;

    protected Hash(final int maxByteCount, final @NonNull String hash) {
        this(maxByteCount, hash, "0x", HEX);
    }

    protected Hash(final int maxByteCount, final @NonNull String hash, final String prefix, final @NonNull Pattern validityPattern) {
        final var validator = Validator.of(maxByteCount, prefix == null ? "" : prefix, validityPattern);
        validator.check(hash);

        this.inner = validator.sanitize(hash);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Hash hash))
            return false;

        return inner.equalsIgnoreCase(hash.inner);
    }

    @Override
    public int hashCode() {
        return inner.hashCode();
    }

    @Override
    public String toString() {
        return inner;
    }

    @AllArgsConstructor(staticName = "of")
    @EqualsAndHashCode
    private static class Validator {
        private final @NonNull Integer maxByteCount;
        private final @NonNull String prefix;
        private final @NonNull Pattern pattern;

        public void check(final @NonNull String hash) {
            final var hashPrefix = hash.substring(0, prefix.length());
            if (!hashPrefix.equalsIgnoreCase(prefix))
                throw badRequest("Provided hash should start with %s".formatted(prefix));

            final var hashBody = hash.substring(prefix.length());
            if (hashBody.isEmpty())
                throw badRequest("Provided hash is too short");
            if (hashBody.length() > maxByteCount * 2)
                throw badRequest("Provided hash should be less than %d bytes".formatted(maxByteCount));
            if (!pattern.matcher(hashBody).matches())
                throw badRequest("Provided hash is not valid");
        }

        public String sanitize(final @NonNull String hash) {
            return prefix + ((hash.length() - prefix.length()) % 2 == 0 ? "" : "0") + hash.substring(prefix.length());
        }
    }
}
