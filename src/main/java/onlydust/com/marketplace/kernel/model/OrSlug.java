package onlydust.com.marketplace.kernel.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.regex.Pattern;

@EqualsAndHashCode
@SuperBuilder
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class OrSlug<T extends UuidWrapper> {
    private static final Pattern UUID_REGEX = Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");

    protected final T id;
    protected final String slug;

    public static <T extends UuidWrapper> OrSlug<T> of(T id) {
        return new OrSlug<>(id);
    }

    public static <T extends UuidWrapper> OrSlug<T> of(String uuidOrSlug, Function<UUID, T> constructor) {
        return new OrSlug<>(uuidOrSlug, constructor);
    }

    protected OrSlug(T id) {
        this.id = id;
        this.slug = null;
    }

    protected OrSlug(String uuidOrSlug, Function<UUID, T> constructor) {
        if (UUID_REGEX.matcher(uuidOrSlug).matches()) {
            this.id = constructor.apply(UUID.fromString(uuidOrSlug));
            this.slug = null;
        } else {
            this.id = null;
            this.slug = uuidOrSlug;
        }
    }

    @Override
    public String toString() {
        return id != null ? id.toString() : slug;
    }

    public Optional<T> id() {
        return Optional.ofNullable(id);
    }

    public Optional<UUID> uuid() {
        return Optional.ofNullable(id).map(UuidWrapper::value);
    }

    public Optional<String> slug() {
        return Optional.ofNullable(slug);
    }
}
