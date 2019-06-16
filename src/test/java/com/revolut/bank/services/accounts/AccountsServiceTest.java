package com.revolut.bank.services.accounts;

import com.revolut.bank.repositories.AccountsRepository;
import com.revolut.bank.services.accounts.domain.Account;
import com.revolut.bank.services.accounts.exceptions.MissingEmailException;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

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
    public void callsBankRecordsRepositoryToRegisterNewAccount() {
        Account account = this.accountsService.create("john.doe@email.com");

        verify(accountsRepository).save(account);
    }

}
