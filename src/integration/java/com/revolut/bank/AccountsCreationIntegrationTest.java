package com.revolut.bank;

import com.revolut.bank.api.accounts.responses.AccountDetailsResponseBody;
import com.revolut.bank.utils.CreateAccount;
import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.Response;
import java.io.IOException;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.CONFLICT;
import static javax.ws.rs.core.Response.Status.CREATED;
import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;
import static org.assertj.core.api.Assertions.assertThat;

public class AccountsCreationIntegrationTest {

    private HttpServer server;

    @Before
    public void setUp() throws IOException {
        this.server = Application.start();
    }

    @After
    public void tearDown() {
        this.server.shutdown();
    }

    @Test
    public void createANewAccount() {
        Response response = CreateAccount.with("john.doe@email.com");

        assertThat(response.getMediaType()).isEqualTo(APPLICATION_JSON_TYPE);
        assertThat(response.getStatus()).isEqualTo(CREATED.getStatusCode());

        AccountDetailsResponseBody responseBody = response.readEntity(AccountDetailsResponseBody.class);

        assertThat(responseBody.getEmail()).isEqualTo("john.doe@email.com");
        assertThat(responseBody.getBalance()).isEqualTo("100.00");
    }

    @Test
    public void returnsBadRequestWhenEmailIsEmpty() {
        Response response = CreateAccount.with("");

        assertThat(response.getMediaType()).isEqualTo(APPLICATION_JSON_TYPE);
        assertThat(response.getStatus()).isEqualTo(BAD_REQUEST.getStatusCode());

        String responseBody = response.readEntity(String.class);

        assertThat(responseBody).isEqualTo("{\"error\":\"Email is required when creating an account\"}");
    }

    @Test
    public void returnsConflictWhenCreatingMultipleAccountsWithTheSameEmail() {
        CreateAccount.with("john.doe@email.com");
        Response response = CreateAccount.with("john.doe@email.com");

        assertThat(response.getMediaType()).isEqualTo(APPLICATION_JSON_TYPE);
        assertThat(response.getStatus()).isEqualTo(CONFLICT.getStatusCode());

        String responseBody = response.readEntity(String.class);

        assertThat(responseBody).isEqualTo("{\"error\":\"An account already exists with this email.\"}");
    }

    @Test
    public void returnsInternalServerErrorWhenRequestIsNull() {
        Response response = CreateAccount.withEmptyRequestBody();

        assertThat(response.getStatus()).isEqualTo(INTERNAL_SERVER_ERROR.getStatusCode());
    }

}
