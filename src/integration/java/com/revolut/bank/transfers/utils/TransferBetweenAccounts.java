package com.revolut.bank.transfers.utils;

import com.revolut.bank.api.transfers.requests.TransferRequestBody;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;

import static com.revolut.bank.accounts.utils.Constants.TRANSFERS_ENDPOINT;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

public class TransferBetweenAccounts {

    public static Response using(String sourceEmail, String destinationEmail, BigDecimal amount) {
        TransferRequestBody transferRequestBody = new TransferRequestBody(sourceEmail, destinationEmail, amount);

        return ClientBuilder.newClient()
                .target(TRANSFERS_ENDPOINT)
                .request(APPLICATION_JSON)
                .post(Entity.entity(transferRequestBody, APPLICATION_JSON));
    }

}
