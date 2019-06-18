package com.revolut.bank.api.accounts;

import com.revolut.bank.api.accounts.requests.CreateAccountRequestBody;
import com.revolut.bank.services.AccountsService;
import com.revolut.bank.services.domain.Account;
import com.revolut.bank.services.exceptions.AccountNotFoundException;
import com.revolut.bank.services.exceptions.MissingEmailException;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.Response;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static javax.ws.rs.core.Response.Status.CREATED;
import static javax.ws.rs.core.Response.Status.OK;
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
    public void throwsMissingEmailExceptionWhenEmailIsNullWhenCreatingAnAccount() {
        when(accountsService.create(any())).thenThrow(new MissingEmailException());

        CreateAccountRequestBody createAccountRequestBody = new CreateAccountRequestBody(null);

        Throwable throwable = catchThrowable(() -> {
            this.controller.createAccount(createAccountRequestBody);
        });

        assertThat(throwable).isInstanceOf(MissingEmailException.class);
    }

    @Test
    public void throwsMissingEmailExceptionWhenEmailIsBlankWhenCreatingAnAccount() {
        when(accountsService.create(any())).thenThrow(new MissingEmailException());

        CreateAccountRequestBody createAccountRequestBody = new CreateAccountRequestBody("");

        Throwable throwable = catchThrowable(() -> {
            this.controller.createAccount(createAccountRequestBody);
        });

        assertThat(throwable).isInstanceOf(MissingEmailException.class);
    }

    @Test
    public void callsAccountsServiceWhenCreatingANewAccount() {
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

    @Test
    public void throwsMissingEmailExceptionWhenEmailIsNullWhenRetrievingAnAccount() {
        when(accountsService.retrieveDetailsOf(any())).thenThrow(new MissingEmailException());

        String email = null;

        Throwable throwable = catchThrowable(() -> {
            this.controller.retrieveAccountDetails(email);
        });

        assertThat(throwable).isInstanceOf(MissingEmailException.class);
    }

    @Test
    public void throwsMissingEmailExceptionWhenEmailIsBlankWhenRetrievingAnAccount() {
        when(accountsService.retrieveDetailsOf(any())).thenThrow(new MissingEmailException());

        String email = "";

        Throwable throwable = catchThrowable(() -> {
            this.controller.retrieveAccountDetails(email);
        });

        assertThat(throwable).isInstanceOf(MissingEmailException.class);
    }

    @Test
    public void throwsAccountNotFoundExceptionWhenRetrievingANonExistentAccount() {
        when(accountsService.retrieveDetailsOf(any())).thenThrow(new AccountNotFoundException());

        Throwable throwable = catchThrowable(() -> {
            this.controller.retrieveAccountDetails("john.doe@email.com");
        });

        assertThat(throwable).isInstanceOf(AccountNotFoundException.class);
    }

    @Test
    public void callsAccountsServiceWhenRetrievingAnAccount() {
        String email = "john.doe@email.com";

        when(accountsService.retrieveDetailsOf(email)).thenReturn(new Account(email));

        this.controller.retrieveAccountDetails(email);

        verify(accountsService).retrieveDetailsOf(email);
    }

    @Test
    public void returnsAccountDetailsWhenRetrievingAnAccount() {
        String email = "john.doe@email.com";

        when(accountsService.retrieveDetailsOf(email)).thenReturn(new Account(email));

        Response response = this.controller.retrieveAccountDetails(email);

        assertThat(response.getEntity()).isNotNull();
        assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
        assertThat(response.getMediaType()).isEqualTo(APPLICATION_JSON_TYPE);
    }

}
