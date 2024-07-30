package onlydust.com.marketplace.kernel.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import onlydust.com.marketplace.kernel.model.notification.NotificationType;
import onlydust.com.marketplace.kernel.model.notification.NotificationTypeIdResolver;
import org.junit.jupiter.api.Test;

import java.io.Serializable;

import static org.assertj.core.api.Assertions.assertThat;

class NotificationTypeIdResolverTest {

    @AllArgsConstructor
    @NoArgsConstructor
    public static class TestNotificationContainer implements Serializable {
        @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "@type")
        @JsonTypeIdResolver(NotificationTypeIdResolver.class)
        private TestNotification testNotification;
    }

    public static abstract class TestNotification {
    }

    @NotificationType("A")
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SubATestNotification extends TestNotification {
        public String foo;
    }

    @NotificationType("B")
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SubBTestNotification extends TestNotification {
        public String bar;
    }

    @Test
    void should_serialize_with_type_id() throws JsonProcessingException {
        final var testContainer = new TestNotificationContainer(new SubATestNotification("yolo"));
        final var mapper = new ObjectMapper();
        final var serialized = mapper.writeValueAsString(testContainer);

        assertThat(serialized).isEqualTo("{\"testNotification\":{\"@type\":\"A\",\"foo\":\"yolo\"}}");
    }

    @Test
    void should_deserialize_with_type_id() throws JsonProcessingException {
        final var mapper = new ObjectMapper();
        final var deserialized = mapper.readValue("{\"testNotification\":{\"@type\":\"B\",\"bar\":\"yolo\"}}", TestNotificationContainer.class);

        assertThat(deserialized).isNotNull();
        assertThat(deserialized.testNotification).isInstanceOf(SubBTestNotification.class);
        assertThat(((SubBTestNotification) deserialized.testNotification).bar).isEqualTo("yolo");
    }
}