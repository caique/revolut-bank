package com.revolut.bank;

import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import java.io.IOException;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.assertj.core.api.Assertions.assertThat;

public class AccountsIntegrationTest {

    public static final String ACCOUNTS_ENDPOINT = Application.BASE_URI + "accounts";

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
    public void accountsServiceIsAvailable() {
        Response response = ClientBuilder.newClient()
                .target(ACCOUNTS_ENDPOINT)
                .request()
                .accept(APPLICATION_JSON)
                .post(Entity.entity(null, APPLICATION_JSON));

        assertThat(response.getStatus()).isEqualTo(200);
    }

}
