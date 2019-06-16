package com.revolut.bank.api.accounts;

import com.revolut.bank.api.accounts.requests.CreateAccountRequestBody;
import com.revolut.bank.services.accounts.AccountsService;
import com.revolut.bank.services.accounts.domain.Account;
import com.revolut.bank.services.accounts.exceptions.MissingEmailException;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.Response;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static javax.ws.rs.core.Response.Status.CREATED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AccountsControllerTest {

    private AccountsController controller;
    private AccountsService accountsService;

    @Before
    public void setUp() {
        this.accountsService = mock(AccountsService.class);
        this.controller = new AccountsController(accountsService);
    }

    @Test
    public void throwsInvalidAccountExceptionWhenEmailIsNull() {
        when(accountsService.create(any())).thenThrow(new MissingEmailException());

        CreateAccountRequestBody createAccountRequestBody = new CreateAccountRequestBody(null);

        Throwable throwable = catchThrowable(() -> {
            this.controller.createAccount(createAccountRequestBody);
        });

        assertThat(throwable).isInstanceOf(MissingEmailException.class);
    }

    @Test
    public void throwsInvalidAccountExceptionWhenEmailIsBlank() {
        when(accountsService.create(any())).thenThrow(new MissingEmailException());

        CreateAccountRequestBody createAccountRequestBody = new CreateAccountRequestBody("");

        Throwable throwable = catchThrowable(() -> {
            this.controller.createAccount(createAccountRequestBody);
        });

        assertThat(throwable).isInstanceOf(MissingEmailException.class);
    }

    @Test
    public void callsCreateAccountUseCaseWhenCreatingANewAccount() {
        when(accountsService.create("john.doe@email.com")).thenReturn(new Account("john.doe@email.com"));

        CreateAccountRequestBody createAccountRequestBody = new CreateAccountRequestBody("john.doe@email.com");

        this.controller.createAccount(createAccountRequestBody);

        verify(accountsService).create("john.doe@email.com");
    }

    @Test
    public void returnsAccountDetailsWhenAnAccountIsCreated() {
        when(accountsService.create("john.doe@email.com")).thenReturn(new Account("john.doe@email.com"));

        CreateAccountRequestBody createAccountRequestBody = new CreateAccountRequestBody("john.doe@email.com");

        Response response = this.controller.createAccount(createAccountRequestBody);

        assertThat(response.getEntity()).isNotNull();
        assertThat(response.getStatus()).isEqualTo(CREATED.getStatusCode());
        assertThat(response.getMediaType()).isEqualTo(APPLICATION_JSON_TYPE);
    }

}
