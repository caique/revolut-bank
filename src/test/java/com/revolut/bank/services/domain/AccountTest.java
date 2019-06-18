package com.revolut.bank.services.domain;

import com.revolut.bank.services.exceptions.MissingEmailException;
import com.revolut.bank.services.exceptions.UnprocessableTransferException;
import org.junit.Test;

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

        assertThat(account.getBalance()).isEqualTo(new MoneyAmount(100));
    }

    @Test
    public void twoAccountInstancesWithSameBalanceAndEmailAreEqual() {
        Account firstAccountForJohn = new Account("john.doe@email.com");
        Account secondAccountForJohn = new Account("john.doe@email.com");
        Account accountForJane = new Account("jane.doe@email.com");

        assertThat(firstAccountForJohn).isEqualTo(secondAccountForJohn);
        assertThat(accountForJane).isNotEqualTo(firstAccountForJohn);
        assertThat(accountForJane).isNotEqualTo(secondAccountForJohn);
    }

    @Test
    public void throwsUnprocessableTransferExceptionWhenAmountIsNull() {
        Account accountForJohn = new Account("john.doe@email.com");
        Account accountForJane = new Account("jane.doe@email.com");

        Throwable throwable = catchThrowable(() -> {
            accountForJohn.transferTo(accountForJane, null);
        });

        assertThat(throwable).isInstanceOf(UnprocessableTransferException.class);
        assertThat(throwable.getMessage()).isEqualTo("{\"error\":\"Amount to be transferred must not be null.\"}");
    }

    @Test
    public void throwsUnprocessableTransferExceptionWhenBalanceIsNotPositive() {
        Account accountForJohn = new Account("john.doe@email.com", MoneyAmount.ZERO);
        Account accountForJane = new Account("jane.doe@email.com");

        Throwable throwable = catchThrowable(() -> {
            accountForJohn.transferTo(accountForJane, MoneyAmount.ONE);
        });

        assertThat(throwable).isInstanceOf(UnprocessableTransferException.class);
        assertThat(throwable.getMessage()).isEqualTo("{\"error\":\"Insufficient funds.\"}");
    }

    @Test
    public void throwsUnprocessableTransferExceptionWhenTransferedAmountIsNotPositive() {
        Account accountForJohn = new Account("john.doe@email.com");
        Account accountForJane = new Account("jane.doe@email.com");

        Throwable throwable = catchThrowable(() -> {
            accountForJohn.transferTo(accountForJane, MoneyAmount.ZERO);
        });

        assertThat(throwable).isInstanceOf(UnprocessableTransferException.class);
        assertThat(throwable.getMessage()).isEqualTo("{\"error\":\"Amount to be transferred must be higher than zero.\"}");
    }

    @Test
    public void throwsUnprocessableTransferExceptionWhenTransferedAmountIsHigherThanBalance() {
        Account accountForJohn = new Account("john.doe@email.com", MoneyAmount.ONE);
        Account accountForJane = new Account("jane.doe@email.com");

        Throwable throwable = catchThrowable(() -> {
            accountForJohn.transferTo(accountForJane, MoneyAmount.GRAND);
        });

        assertThat(throwable).isInstanceOf(UnprocessableTransferException.class);
        assertThat(throwable.getMessage()).isEqualTo("{\"error\":\"Insufficient funds.\"}");
    }

    @Test
    public void throwsUnprocessableTransferExceptionWhenSourceAndDestinationAccountsAreTheSame() {
        Account accountForJohn = new Account("john.doe@email.com");

        Throwable throwable = catchThrowable(() -> {
            accountForJohn.transferTo(accountForJohn, MoneyAmount.GRAND);
        });

        assertThat(throwable).isInstanceOf(UnprocessableTransferException.class);
        assertThat(throwable.getMessage()).isEqualTo("{\"error\":\"Only transfers between different accounts are possible.\"}");
    }

    @Test
    public void balanceMustBeUpdatedAfterTransfer() {
        Account accountForJohn = new Account("john.doe@email.com", MoneyAmount.ONE);
        Account accountForJane = new Account("jane.doe@email.com", MoneyAmount.ZERO);

        accountForJohn.transferTo(accountForJane, MoneyAmount.ONE);

        assertThat(accountForJohn.getBalance()).isEqualTo(MoneyAmount.ZERO);
        assertThat(accountForJane.getBalance()).isEqualTo(MoneyAmount.ONE);
    }

}
