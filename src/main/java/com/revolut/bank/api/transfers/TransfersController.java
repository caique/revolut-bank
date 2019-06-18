package com.revolut.bank.api.transfers;

import com.revolut.bank.api.accounts.responses.AccountDetailsResponseBody;
import com.revolut.bank.api.transfers.requests.TransferRequestBody;
import com.revolut.bank.services.TransfersService;
import com.revolut.bank.services.domain.Account;
import com.revolut.bank.services.domain.MoneyAmount;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.HttpHeaders.CONTENT_TYPE;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.OK;

@Path("/transfers")
public class TransfersController {

    private final TransfersService transfersService;

    @Inject
    public TransfersController(TransfersService transfersService) {
        this.transfersService = transfersService;
    }

    @POST
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public Response transfer(TransferRequestBody requestBody) {
        String source = requestBody.getSource();
        String destination = requestBody.getDestination();
        MoneyAmount amount = requestBody.getMoneyAmount();

        Account account = this.transfersService.transferBetweenAccounts(source, destination, amount);

        return Response.status(OK)
                .entity(new AccountDetailsResponseBody(account))
                .header(CONTENT_TYPE, APPLICATION_JSON)
                .build();
    }

}
