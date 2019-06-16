package com.revolut.bank.api.accounts.responses;

import com.revolut.bank.services.accounts.domain.Account;
import org.junit.Test;

import static com.revolut.bank.api.accounts.responses.AccountDetailsResponseBody.NUMBER_OF_DECIMAL_PLACES;
import static com.revolut.bank.api.accounts.responses.AccountDetailsResponseBody.ROUNDING_STRATEGY;
import static org.assertj.core.api.Assertions.assertThat;

public class AccountDetailsResponseBodyTest {

    @Test
    public void returnsSameDetailsAsTheAccountProvidedInTheConstructor() {
        Account account = new Account("john.doe@email.com");
        AccountDetailsResponseBody accountDetailsResponseBody = new AccountDetailsResponseBody(account);

        String expectedEmail = account.getEmail();
        String expectedBalance = account.getBalance().setScale(NUMBER_OF_DECIMAL_PLACES, ROUNDING_STRATEGY).toString();

        assertThat(accountDetailsResponseBody.getEmail()).isEqualTo(expectedEmail);
        assertThat(accountDetailsResponseBody.getBalance()).isEqualTo(expectedBalance);
    }

}
