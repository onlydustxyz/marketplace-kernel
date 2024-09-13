package onlydust.com.marketplace.kernel.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.experimental.SuperBuilder;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@NoArgsConstructor(staticName = "random")
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
class Id extends UuidWrapper {
    public static Id of(@NonNull final UUID uuid) {
        return Id.builder().uuid(uuid).build();
    }

    @JsonCreator
    public static Id of(@NonNull final String uuid) {
        return Id.of(UUID.fromString(uuid));
    }
}

class UuidWrapperTest {
    @Test
    void should_be_equal() {
        final var uuid = UUID.randomUUID();
        final var id1 = Id.of(uuid);
        final var id2 = Id.of(uuid);
        assertThat(id1).isEqualTo(id2);
    }

    @Test
    void should_display_inner_value() {
        final var id = Id.of(UUID.fromString("2650826c-9ac1-4f49-a218-8647f851089f"));
        assertThat("%s".formatted(id)).isEqualTo("2650826c-9ac1-4f49-a218-8647f851089f");
    }

    @Test
    void can_be_passed_to_function() {
        final var uuid = UUID.randomUUID();
        final var id = Id.of(uuid);
        f(id);
    }

    @Test
    void should_allow_creation_from_random() {
        final var id1 = Id.random();
        final var id2 = Id.random();
        assertThat(id1).isNotEqualTo(id2);
    }

    void f(Id id) {
        System.out.println(id);
    }

    @SneakyThrows
    @Test
    void can_be_serialized() {
        final var record = new Record(Id.of("2650826c-9ac1-4f49-a218-8647f851089f"));
        final var json = new ObjectMapper().writeValueAsString(record);
        assertThat(json).isEqualTo("{\"id\":{\"uuid\":\"2650826c-9ac1-4f49-a218-8647f851089f\"}}");
    }

    @SneakyThrows
    @Test
    void can_be_deserialized_from_object() {
        final var json = """
                {
                    "id": {
                        "uuid": "2650826c-9ac1-4f49-a218-8647f851089f"
                    }
                }
                """;
        final var record = new ObjectMapper().readValue(json, Record.class);
        assertThat(record.id().toString()).isEqualTo("2650826c-9ac1-4f49-a218-8647f851089f");
    }

    @SneakyThrows
    @Test
    void can_be_deserialized_from_string() {
        final var json = """
                {
                    "id": "2650826c-9ac1-4f49-a218-8647f851089f"
                }
                """;
        final var record = new ObjectMapper().readValue(json, Record.class);
        assertThat(record.id().toString()).isEqualTo("2650826c-9ac1-4f49-a218-8647f851089f");
    }

    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    record Record(Id id) {
    }
}

