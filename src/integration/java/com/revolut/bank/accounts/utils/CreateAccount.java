package com.revolut.bank.accounts.utils;

import com.revolut.bank.api.accounts.requests.CreateAccountRequestBody;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

import static com.revolut.bank.accounts.utils.Constants.ACCOUNTS_ENDPOINT;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

public class CreateAccount {

    public static Response with(String email) {
        CreateAccountRequestBody createAccountRequestBody = new CreateAccountRequestBody(email);

        return ClientBuilder.newClient()
                .target(ACCOUNTS_ENDPOINT)
                .request(APPLICATION_JSON)
                .post(Entity.entity(createAccountRequestBody, APPLICATION_JSON));
    }

    public static Response withEmptyRequestBody() {
        return ClientBuilder.newClient()
                .target(ACCOUNTS_ENDPOINT)
                .request(APPLICATION_JSON)
                .post(Entity.entity(null, APPLICATION_JSON));
    }

}
