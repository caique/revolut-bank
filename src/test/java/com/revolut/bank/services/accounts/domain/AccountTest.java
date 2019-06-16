package com.revolut.bank.services.accounts.domain;

import com.revolut.bank.services.accounts.exceptions.MissingEmailException;
import org.junit.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class AccountTest {

    @Test
    public void throwsMissingEmailExceptionWhenEmailIsNull() {
        Throwable throwable = catchThrowable(() -> {
            new Account(null);
        });

        assertThat(throwable).isInstanceOf(MissingEmailException.class);
    }

    @Test
    public void throwsMissingEmailExceptionWhenEmailIsBlank() {
        Throwable throwable = catchThrowable(() -> {
            new Account("");
        });

        assertThat(throwable).isInstanceOf(MissingEmailException.class);
    }

    @Test
    public void anAccountHasTheSameEmailProvidedInTheConstructor() {
        Account account = new Account("john.doe@email.com");

        assertThat(account.getEmail()).isEqualTo("john.doe@email.com");
    }

    @Test
    public void theBuiltAccountMustHaveBalanceEqualsTo100() {
        Account account = new Account("john.doe@email.com");

        assertThat(account.getBalance()).isEqualTo(new BigDecimal(100.00));
    }

}
