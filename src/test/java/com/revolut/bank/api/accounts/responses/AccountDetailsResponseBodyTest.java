package com.revolut.bank.api.accounts.responses;

import com.revolut.bank.services.domain.Account;
import com.revolut.bank.services.domain.MoneyAmount;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AccountDetailsResponseBodyTest {

    @Test
    public void returnsSameDetailsAsTheAccountProvidedInTheConstructor() {
        Account account = new Account("john.doe@email.com", MoneyAmount.GRAND);
        AccountDetailsResponseBody accountDetailsResponseBody = new AccountDetailsResponseBody(account);

        assertThat(accountDetailsResponseBody.getEmail()).isEqualTo("john.doe@email.com");
        assertThat(accountDetailsResponseBody.getBalance()).isEqualTo("100.00");
    }

}
