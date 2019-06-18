package com.revolut.bank;

import com.revolut.bank.api.accounts.responses.AccountDetailsResponseBody;
import com.revolut.bank.utils.CreateAccount;
import com.revolut.bank.utils.TransferBetweenAccounts;
import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.math.BigDecimal;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static javax.ws.rs.core.Response.Status.OK;
import static org.assertj.core.api.Assertions.assertThat;

public class TransferBetweenAccountsIntegrationTest {

    private static final int UNPROCESSABLE_ENTITY = 422;

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
    public void transferMoneyBetweenTwoAccounts() {
        String emailForJohn = "john.doe@email.com";
        String emailForJane = "jane.doe@email.com";
        BigDecimal amountToBeTransferred = BigDecimal.ONE;

        CreateAccount.with(emailForJohn);
        CreateAccount.with(emailForJane);

        Response response = TransferBetweenAccounts.using(emailForJohn, emailForJane, amountToBeTransferred);

        assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
        assertThat(response.getMediaType()).isEqualTo(APPLICATION_JSON_TYPE);

        AccountDetailsResponseBody responseBody = response.readEntity(AccountDetailsResponseBody.class);

        assertThat(responseBody.getEmail()).isEqualTo("john.doe@email.com");
        assertThat(responseBody.getBalance()).isEqualTo("99.00");
    }

    @Test
    public void returnsUnprocessableEntityWhenAttemptToTransferMoreMoneyThanTheCurrentBalance() {
        String emailForJohn = "john.doe@email.com";
        String emailForJane = "jane.doe@email.com";
        BigDecimal amountToBeTransferred = new BigDecimal(999.99);

        CreateAccount.with(emailForJohn);
        CreateAccount.with(emailForJane);

        Response response = TransferBetweenAccounts.using(emailForJohn, emailForJane, amountToBeTransferred);

        assertThat(response.getStatus()).isEqualTo(UNPROCESSABLE_ENTITY);
        assertThat(response.getMediaType()).isEqualTo(APPLICATION_JSON_TYPE);

        String responseBody = response.readEntity(String.class);

        assertThat(responseBody).isEqualTo("{\"error\":\"Insufficient funds.\"}");
    }

    @Test
    public void returnsUnprocessableEntityWhenAttemptToTransferMoneyFromANonExistentAccount() {
        String emailOfNonExistentAccount = "john.doe@email.com";
        String emailForJane = "jane.doe@email.com";
        BigDecimal amountToBeTransferred = BigDecimal.ONE;

        CreateAccount.with(emailForJane);

        Response response = TransferBetweenAccounts.using(emailOfNonExistentAccount, emailForJane, amountToBeTransferred);

        assertThat(response.getStatus()).isEqualTo(UNPROCESSABLE_ENTITY);
        assertThat(response.getMediaType()).isEqualTo(APPLICATION_JSON_TYPE);

        String responseBody = response.readEntity(String.class);

        assertThat(responseBody).isEqualTo("{\"error\":\"An unexpected error occurred and the transfer was not processed.\"}");
    }

    @Test
    public void returnsUnprocessableEntityWhenAttemptToTransferMoneyToNonExistentAccount() {
        String emailForJohn = "john.doe@email.com";
        String emailOfNonExistentAccount = "jane.doe@email.com";
        BigDecimal amountToBeTransferred = BigDecimal.ONE;

        CreateAccount.with(emailForJohn);

        Response response = TransferBetweenAccounts.using(emailForJohn, emailOfNonExistentAccount, amountToBeTransferred);

        assertThat(response.getStatus()).isEqualTo(UNPROCESSABLE_ENTITY);
        assertThat(response.getMediaType()).isEqualTo(APPLICATION_JSON_TYPE);

        String responseBody = response.readEntity(String.class);

        assertThat(responseBody).isEqualTo("{\"error\":\"An unexpected error occurred and the transfer was not processed.\"}");
    }

}
