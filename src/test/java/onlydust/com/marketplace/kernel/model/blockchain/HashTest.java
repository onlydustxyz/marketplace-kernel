package onlydust.com.marketplace.kernel.model.blockchain;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class HashTest {

    static class PrefixedHash extends Hash {
        public PrefixedHash(final String hash) {
            super(4, hash);
        }
    }

    static class NonPrefixedHash extends Hash {
        public NonPrefixedHash(final String hash) {
            super(4, hash, null, HEX);
        }
    }

    static class CustomPrefixedHash extends Hash {
        public CustomPrefixedHash(final String hash) {
            super(4, hash, "xxx", HEX);
        }
    }

    @Nested
    class PrefixedHashTest {
        @Test
        void should_accept_valid_hash() {
            new PrefixedHash("0x12345678");
            new PrefixedHash("0X12345678");
        }

        @Test
        void should_reject_invalid_hash() {
            assertThatThrownBy(() -> new PrefixedHash("12345678")).hasMessage("Provided hash should start with 0x");
            assertThatThrownBy(() -> new PrefixedHash("0x123456798123")).hasMessage("Provided hash should be less than 4 bytes");
            assertThatThrownBy(() -> new PrefixedHash("0x")).hasMessage("Provided hash is too short");
            assertThatThrownBy(() -> new PrefixedHash("0x1k")).hasMessage("Provided hash is not valid");
        }

        @Test
        void should_sanitize_hash() {
            assertThat(new PrefixedHash("0x012345").toString()).isEqualTo("0x012345");
            assertThat(new PrefixedHash("0x012345").toString()).isEqualTo("0x012345");
            assertThat(new PrefixedHash("0X12345").toString()).isEqualTo("0x012345");
            assertThat(new PrefixedHash("0X12345").toString()).isEqualTo("0x012345");
        }
    }


    @Nested
    class NonPrefixedHashTest {
        @Test
        void should_accept_valid_hash() {
            new NonPrefixedHash("12345678");
        }

        @Test
        void should_reject_invalid_hash() {
            assertThatThrownBy(() -> new NonPrefixedHash("0x1234")).hasMessage("Provided hash is not valid");
            assertThatThrownBy(() -> new NonPrefixedHash("123456798123")).hasMessage("Provided hash should be less than 4 bytes");
            assertThatThrownBy(() -> new NonPrefixedHash("")).hasMessage("Provided hash is too short");
            assertThatThrownBy(() -> new NonPrefixedHash("1k")).hasMessage("Provided hash is not valid");
        }

        @Test
        void should_sanitize_hash() {
            assertThat(new NonPrefixedHash("012345").toString()).isEqualTo("012345");
            assertThat(new NonPrefixedHash("12345").toString()).isEqualTo("012345");
        }
    }

    @Nested
    class CustomPrefixedHashTest {
        @Test
        void should_accept_valid_hash() {
            new CustomPrefixedHash("xxx12345678");
            new CustomPrefixedHash("XXx12345678");
            new CustomPrefixedHash("XXX12345678");
        }

        @Test
        void should_reject_invalid_hash() {
            assertThatThrownBy(() -> new CustomPrefixedHash("1234")).hasMessage("Provided hash should start with xxx");
            assertThatThrownBy(() -> new CustomPrefixedHash("xxx123456798123")).hasMessage("Provided hash should be less than 4 bytes");
            assertThatThrownBy(() -> new CustomPrefixedHash("xxx")).hasMessage("Provided hash is too short");
            assertThatThrownBy(() -> new CustomPrefixedHash("xxx1k")).hasMessage("Provided hash is not valid");
        }

        @Test
        void should_sanitize_hash() {
            assertThat(new CustomPrefixedHash("xxx012345").toString()).isEqualTo("xxx012345");
            assertThat(new CustomPrefixedHash("xxx012345").toString()).isEqualTo("xxx012345");
            assertThat(new CustomPrefixedHash("XxX12345").toString()).isEqualTo("xxx012345");
            assertThat(new CustomPrefixedHash("XxX12345").toString()).isEqualTo("xxx012345");
        }
    }
}
