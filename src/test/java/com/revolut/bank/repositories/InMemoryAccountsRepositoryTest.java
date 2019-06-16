package com.revolut.bank.repositories;

import com.revolut.bank.services.accounts.domain.Account;
import com.revolut.bank.services.accounts.exceptions.DuplicatedAccountException;
import org.junit.Before;
import org.junit.Test;

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
    public void saveAccountsWithNewEmailsWithoutThrowingExceptions() {
        Account accountForJohn = new Account("john.doe@email.com");
        Account accountForJane = new Account("jane.doe@email.com");

        Throwable throwable = catchThrowable(() -> {
            accountsRepository.save(accountForJohn);
            accountsRepository.save(accountForJane);
        });

        assertThat(throwable).isNull();
    }

}
