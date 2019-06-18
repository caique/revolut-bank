package com.revolut.bank;

import com.revolut.bank.api.accounts.responses.AccountDetailsResponseBody;
import com.revolut.bank.utils.CreateAccount;
import com.revolut.bank.utils.RetrieveAccountDetails;
import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.Response;
import java.io.IOException;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.OK;
import static org.assertj.core.api.Assertions.assertThat;

public class AccountsRetrievalIntegrationTest {

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
    public void retrieveAccountDetails() {
        CreateAccount.with("john.doe@email.com");

        Response response = RetrieveAccountDetails.with("john.doe@email.com");

        assertThat(response.getMediaType()).isEqualTo(APPLICATION_JSON_TYPE);
        assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());

        AccountDetailsResponseBody responseBody = response.readEntity(AccountDetailsResponseBody.class);

        assertThat(responseBody.getEmail()).isEqualTo("john.doe@email.com");
        assertThat(responseBody.getBalance()).isEqualTo("100.00");
    }

    @Test
    public void returnsNotFoundWhenAttemptToRetrieveDetailsOfANonExistentAccount() {
        String emailOfNonExistentAccount = "jane.doe@email.com";

        Response response = RetrieveAccountDetails.with(emailOfNonExistentAccount);

        assertThat(response.getStatus()).isEqualTo(NOT_FOUND.getStatusCode());
        assertThat(response.getMediaType()).isEqualTo(APPLICATION_JSON_TYPE);

        String responseBody = response.readEntity(String.class);

        assertThat(responseBody).isEqualTo("{\"error\":\"Account does not exist.\"}");
    }

}
