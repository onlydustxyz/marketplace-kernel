package onlydust.com.marketplace.kernel.model.blockchain;

import onlydust.com.marketplace.kernel.exception.OnlyDustException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StellarTest {
    @ParameterizedTest()
    @ValueSource(strings = {"", "02937", "0x", "0x0asdf", "0x12345678901234567890123456789012345678901234567890123456789012345"})
    void should_reject_invalid_account(String value) {
        assertThatThrownBy(() -> Stellar.accountId(value)).isInstanceOf(OnlyDustException.class);
    }

    @Test
    void should_accept_valid_account() {
        assertDoesNotThrow(() -> Stellar.accountId("GA6MC3D6BNEFHZBYROFJ67O6TSZ2JZCDH3Y2PFJUUIDOEX26HDBHD4PB"));
    }

    @ParameterizedTest()
    @ValueSource(strings = {"", "Z02937", "0x", "0x0asdf", "0x01234567890123456789012345678901234567890123456789012345678901234"})
    void should_reject_invalid_hash(String value) {
        assertThrows(OnlyDustException.class, () -> Stellar.transactionHash(value));
    }

    @Test
    void should_accept_valid_hash() {
        assertDoesNotThrow(() -> Stellar.transactionHash("e39906d57d0803f9af7d0d6e0b86c68e6662d26e4a8915c132d50d72869dcc0e"));
    }

    @Test
    void should_generate_transaction_url() {
        assertThat(Stellar.BLOCK_EXPLORER.url(Stellar.transactionHash("123")).toString()).isEqualTo("https://stellar.expert/explorer/public/tx/0123");
    }

    @Test
    void should_create_contract_address() {
        final var contractAddress = Stellar.contractAddress("CBEOJUP5FU6KKOEZ7RMTSKZ7YLBS5D6LVATIGCESOGXSZEQ2UWQFKZW6");
        assertThat(contractAddress.toString()).isEqualTo("CBEOJUP5FU6KKOEZ7RMTSKZ7YLBS5D6LVATIGCESOGXSZEQ2UWQFKZW6");
    }
}