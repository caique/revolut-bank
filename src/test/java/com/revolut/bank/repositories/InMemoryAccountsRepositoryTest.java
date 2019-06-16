package com.revolut.bank.repositories;

import com.revolut.bank.services.domain.Account;
import com.revolut.bank.services.exceptions.AccountNotFoundException;
import com.revolut.bank.services.exceptions.DuplicatedAccountException;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class InMemoryAccountsRepositoryTest {

    private AccountsRepository accountsRepository;

    @Before
    public void setUp() {
        this.accountsRepository = new InMemoryAccountsRepository();
    }

    @Test
    public void throwsDuplicatedAccountExceptionWhenAttemptToSaveAnAccountWithADuplicatedEmail() {
        Account account = new Account("john.doe@email.com");

        accountsRepository.save(account);

        Throwable throwable = catchThrowable(() -> {
            accountsRepository.save(account);
        });

        assertThat(throwable).isInstanceOf(DuplicatedAccountException.class);
    }

    @Test
    public void saveAccountsWithNewEmailWithoutThrowingExceptions() {
        Account accountForJohn = new Account("john.doe@email.com");
        Account accountForJane = new Account("jane.doe@email.com");

        Throwable throwable = catchThrowable(() -> {
            accountsRepository.save(accountForJohn);
            accountsRepository.save(accountForJane);
        });

        assertThat(throwable).isNull();
    }

    @Test
    public void throwsAccountNotFoundExceptionWhenAttemptToUpdateANonExistentAccount() {
        String email = "john.doe@email.com";

        Throwable throwable = catchThrowable(() -> {
            accountsRepository.update(new Account(email));
        });

        assertThat(throwable).isInstanceOf(AccountNotFoundException.class);
    }

    @Test
    public void updateAccountBalanceWithoutThrowingExceptions() {
        String email = "john.doe@email.com";
        BigDecimal newBalance = new BigDecimal(90.00);

        accountsRepository.save(new Account(email));

        Account modifiedAccount = new Account(email, newBalance);

        Throwable throwable = catchThrowable(() -> {
            accountsRepository.update(modifiedAccount);
        });

        Account account = accountsRepository.findByEmail(email);

        assertThat(account).isNotNull();
        assertThat(account.getEmail()).isEqualTo(email);
        assertThat(account.getBalance().toString()).isEqualTo("90.00");
    }

    @Test
    public void returnAccountWhenFindingByEmail() {
        String email = "john.doe@email.com";

        accountsRepository.save(new Account(email));

        Account account = accountsRepository.findByEmail(email);

        assertThat(account).isNotNull();
        assertThat(account.getEmail()).isEqualTo(email);
    }

    @Test
    public void returnNullWhenAccountIsNotFound() {
        String nonExistentEmail = "john.doe@email.com";

        Account account = accountsRepository.findByEmail(nonExistentEmail);

        assertThat(account).isNull();
    }

}
