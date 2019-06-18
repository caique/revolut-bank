package com.revolut.bank.utils;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

public class RetrieveAccountDetails {

    public static Response with(String email) {
        return ClientBuilder.newClient()
                .target(Constants.ACCOUNTS_ENDPOINT)
                .path(email)
                .request(APPLICATION_JSON)
                .get();
    }

}
