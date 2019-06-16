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

import static org.assertj.core.api.Assertions.assertThat;

public class MultipleTransfersIntegrationTest {

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
    public void executeMultipleTransfers() {
        String emailForJohn = "john.doe@email.com";
        String emailForJane = "jane.doe@email.com";

        CreateAccount.with(emailForJohn);
        CreateAccount.with(emailForJane);

        TransferBetweenAccounts.using(emailForJohn, emailForJane, new BigDecimal(999.99));
        TransferBetweenAccounts.using(emailForJohn, emailForJohn, new BigDecimal(999.99));
        TransferBetweenAccounts.using(emailForJane, emailForJane, new BigDecimal(999.99));

        TransferBetweenAccounts.using(emailForJane, emailForJohn, BigDecimal.TEN);
        TransferBetweenAccounts.using(emailForJane, emailForJohn, BigDecimal.TEN);
        TransferBetweenAccounts.using(emailForJane, emailForJohn, BigDecimal.TEN);

        TransferBetweenAccounts.using(emailForJohn, emailForJane, BigDecimal.TEN);
        TransferBetweenAccounts.using(emailForJohn, emailForJane, BigDecimal.TEN);
        TransferBetweenAccounts.using(emailForJohn, emailForJane, BigDecimal.TEN);

        TransferBetweenAccounts.using(emailForJohn, emailForJane, new BigDecimal(42.24));
        TransferBetweenAccounts.using(emailForJane, emailForJohn, new BigDecimal(21.12));
        TransferBetweenAccounts.using(emailForJane, emailForJohn, new BigDecimal(21.12));

        Response finalAccountDetail = TransferBetweenAccounts.using(emailForJane, emailForJohn, new BigDecimal(50.0));

        AccountDetailsResponseBody accountDetails = finalAccountDetail.readEntity(AccountDetailsResponseBody.class);

        assertThat(accountDetails.getBalance()).isEqualTo("50.00");
    }

}
