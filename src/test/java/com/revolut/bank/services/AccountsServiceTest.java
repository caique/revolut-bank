package com.revolut.bank.services;

import com.revolut.bank.repositories.AccountsRepository;
import com.revolut.bank.services.domain.Account;
import com.revolut.bank.services.exceptions.AccountNotFoundException;
import com.revolut.bank.services.exceptions.MissingEmailException;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AccountsServiceTest {

    private AccountsService accountsService;
    private AccountsRepository accountsRepository;

    @Before
    public void setUp() {
        this.accountsRepository = mock(AccountsRepository.class);
        this.accountsService = new AccountsService(accountsRepository);
    }

    @Test
    public void throwsMissingEmailExceptionWhenCreatingAccountWithNullAsEmail() {
        Throwable throwable = catchThrowable(() -> {
            this.accountsService.create(null);
        });

        assertThat(throwable).isInstanceOf(MissingEmailException.class);
    }

    @Test
    public void callsAccountsRepositoryToRegisterNewAccount() {
        Account account = this.accountsService.create("john.doe@email.com");

        verify(accountsRepository).save(account);
    }

    @Test
    public void throwsMissingEmailExceptionWhenRetrievingAccountWithNullAsEmail() {
        Throwable throwable = catchThrowable(() -> {
            this.accountsService.retrieveDetailsOf(null);
        });

        assertThat(throwable).isInstanceOf(MissingEmailException.class);
    }

    @Test
    public void throwsMissingEmailExceptionWhenRetrievingAccountWithBlankEmail() {
        Throwable throwable = catchThrowable(() -> {
            this.accountsService.retrieveDetailsOf("");
        });

        assertThat(throwable).isInstanceOf(MissingEmailException.class);
    }

    @Test
    public void callsAccountsRepositoryToRetrieveAccountDetails() {
        String email = "john.doe@email.com";

        Account existentAccountOnRepository = new Account(email);
        when(accountsRepository.findByEmail(email)).thenReturn(existentAccountOnRepository);

        this.accountsService.retrieveDetailsOf(email);

        verify(accountsRepository).findByEmail(email);
    }

    @Test
    public void returnsDetailsWhenAccountsExists() {
        String email = "john.doe@email.com";

        Account existentAccountOnRepository = new Account(email);
        when(accountsRepository.findByEmail(email)).thenReturn(existentAccountOnRepository);

        Account retrievedAccount = this.accountsService.retrieveDetailsOf(email);

        assertThat(retrievedAccount).isEqualTo(existentAccountOnRepository);
    }

    @Test
    public void throwsAccountNotFoundExceptionWhenAccountDoesNotExist() {
        when(accountsRepository.findByEmail(any())).thenThrow(new AccountNotFoundException());

        Throwable throwable = catchThrowable(() -> {
            this.accountsService.retrieveDetailsOf("john.doe@email.com");
        });

        assertThat(throwable).isInstanceOf(AccountNotFoundException.class);
    }

}
