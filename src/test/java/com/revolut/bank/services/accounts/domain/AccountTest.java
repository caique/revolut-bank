package com.revolut.bank.services.accounts.domain;

import com.revolut.bank.services.accounts.exceptions.MissingEmailException;
import com.revolut.bank.services.transfers.domain.MoneyAmount;
import com.revolut.bank.services.transfers.exceptions.UnprocessableTransferException;
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

        assertThat(account.getBalance().toString()).isEqualTo("100.00");
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
        BigDecimal baseAmount = BigDecimal.TEN;

        Account accountForJohn = new Account("john.doe@email.com", BigDecimal.ZERO);
        Account accountForJane = new Account("jane.doe@email.com");

        Throwable throwable = catchThrowable(() -> {
            accountForJohn.transferTo(accountForJane, new MoneyAmount(baseAmount));
        });

        assertThat(throwable).isInstanceOf(UnprocessableTransferException.class);
        assertThat(throwable.getMessage()).isEqualTo("{\"error\":\"Insufficient funds.\"}");
    }

    @Test
    public void throwsUnprocessableTransferExceptionWhenTransferedAmountIsNotPositive() {
        Account accountForJohn = new Account("john.doe@email.com");
        Account accountForJane = new Account("jane.doe@email.com");

        Throwable throwable = catchThrowable(() -> {
            accountForJohn.transferTo(accountForJane, new MoneyAmount(BigDecimal.ZERO));
        });

        assertThat(throwable).isInstanceOf(UnprocessableTransferException.class);
        assertThat(throwable.getMessage()).isEqualTo("{\"error\":\"Amount to be transferred must be higher than zero.\"}");
    }

    @Test
    public void throwsUnprocessableTransferExceptionWhenTransferedAmountIsHigherThanBalance() {
        Account accountForJohn = new Account("john.doe@email.com", BigDecimal.ONE);
        Account accountForJane = new Account("jane.doe@email.com");

        Throwable throwable = catchThrowable(() -> {
            accountForJohn.transferTo(accountForJane, new MoneyAmount(BigDecimal.TEN));
        });

        assertThat(throwable).isInstanceOf(UnprocessableTransferException.class);
        assertThat(throwable.getMessage()).isEqualTo("{\"error\":\"Insufficient funds.\"}");
    }

    @Test
    public void throwsUnprocessableTransferExceptionWhenSourceAndDestinationAccountsAreTheSame() {
        Account accountForJohn = new Account("john.doe@email.com");

        Throwable throwable = catchThrowable(() -> {
            accountForJohn.transferTo(accountForJohn, new MoneyAmount(BigDecimal.TEN));
        });

        assertThat(throwable).isInstanceOf(UnprocessableTransferException.class);
        assertThat(throwable.getMessage()).isEqualTo("{\"error\":\"Only transfers between different accounts are possible.\"}");
    }

    @Test
    public void balanceMustBeUpdatedAfterTransfer() {
        Account accountForJohn = new Account("john.doe@email.com", BigDecimal.ONE);
        Account accountForJane = new Account("jane.doe@email.com", BigDecimal.ZERO);

        accountForJohn.transferTo(accountForJane, new MoneyAmount(BigDecimal.ONE));

        assertThat(accountForJohn.getBalance()).isEqualTo(new MoneyAmount(BigDecimal.ZERO).getAmount());
        assertThat(accountForJane.getBalance()).isEqualTo(new MoneyAmount(BigDecimal.ONE).getAmount());
    }

}
