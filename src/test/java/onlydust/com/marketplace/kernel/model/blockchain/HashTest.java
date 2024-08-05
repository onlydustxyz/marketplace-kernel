package onlydust.com.marketplace.kernel.model.blockchain;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class HashTest {
    @Nested
    class PrefixedHashTest {
        @Test
        void should_accept_valid_hash() {
            new TestHash("0x123456af");
            new TestHash("0X123456AF");
        }

        @Test
        void should_reject_invalid_hash() {
            assertThatThrownBy(() -> new TestHash("12345678")).hasMessage("Provided hash should start with 0x");
            assertThatThrownBy(() -> new TestHash("0x123456798123")).hasMessage("Provided hash should be less than 8 characters");
            assertThatThrownBy(() -> new TestHash("0x")).hasMessage("Provided hash should not be empty");
            assertThatThrownBy(() -> new TestHash("0x1k")).hasMessage("Provided hash is not hexadecimal");
        }

        @Test
        void should_sanitize_hash() {
            assertThat(new TestHash("0x012345").toString()).isEqualTo("0x012345");
            assertThat(new TestHash("0x012345").toString()).isEqualTo("0x012345");
            assertThat(new TestHash("0X12345").toString()).isEqualTo("0x012345");
            assertThat(new TestHash("0X12345").toString()).isEqualTo("0x012345");
        }

        static class TestHash extends PrefixedHexHash {
            public TestHash(final String hash) {
                super(4, hash);
            }
        }
    }

    @Nested
    class NonPrefixedHashTest {
        @Test
        void should_accept_valid_hash() {
            new TestHash("12345678");
        }

        @Test
        void should_reject_invalid_hash() {
            assertThatThrownBy(() -> new TestHash("0x1234")).hasMessage("Provided hash is not hexadecimal");
            assertThatThrownBy(() -> new TestHash("123456798123")).hasMessage("Provided hash should be less than 8 characters");
            assertThatThrownBy(() -> new TestHash("")).hasMessage("Provided hash should not be empty");
            assertThatThrownBy(() -> new TestHash("1k")).hasMessage("Provided hash is not hexadecimal");
        }

        @Test
        void should_sanitize_hash() {
            assertThat(new TestHash("012345").toString()).isEqualTo("012345");
            assertThat(new TestHash("12345").toString()).isEqualTo("012345");
        }

        static class TestHash extends HexHash {
            public TestHash(final String hash) {
                super(4, hash);
            }
        }
    }

    @Nested
    class CustomPrefixedHashTest {
        @Test
        void should_accept_valid_hash() {
            new TestHash("xxx12345678");
            new TestHash("XXx12345678");
            new TestHash("XXX12345678");
        }

        @Test
        void should_reject_invalid_hash() {
            assertThatThrownBy(() -> new TestHash("1234")).hasMessage("Provided hash should start with xxx");
            assertThatThrownBy(() -> new TestHash("xxx123456798123")).hasMessage("Provided hash should be less than 8 characters");
            assertThatThrownBy(() -> new TestHash("xxx")).hasMessage("Provided hash should not be empty");
            assertThatThrownBy(() -> new TestHash("xxx1k")).hasMessage("Provided hash is not hexadecimal");
        }

        @Test
        void should_sanitize_hash() {
            assertThat(new TestHash("xxx012345").toString()).isEqualTo("xxx012345");
            assertThat(new TestHash("xxx012345").toString()).isEqualTo("xxx012345");
            assertThat(new TestHash("XxX12345").toString()).isEqualTo("xxx012345");
            assertThat(new TestHash("XxX12345").toString()).isEqualTo("xxx012345");
        }

        static class TestHash extends PrefixedHexHash {
            public TestHash(final String hash) {
                super(4, "xxx", hash);
            }
        }
    }

    @Nested
    class Base32HashTest {
        @Test
        void should_accept_valid_hash() {
            new TestHash("AHYTD327");
        }

        @Test
        void should_reject_invalid_hash() {
            assertThatThrownBy(() -> new TestHash("HTYDF3652W")).hasMessage("Provided hash should be less than 8 characters");
            assertThatThrownBy(() -> new TestHash("")).hasMessage("Provided hash should not be empty");
            assertThatThrownBy(() -> new TestHash("12")).hasMessage("Provided hash is not base 32");
        }

        @Test
        void should_sanitize_hash() {
            assertThat(new TestHash("ABC").toString()).isEqualTo("ABC");
            assertThat(new TestHash("ABCD").toString()).isEqualTo("ABCD");
        }

        static class TestHash extends Base32Hash {
            public TestHash(final String hash) {
                super(8, hash);
            }
        }
    }
}
