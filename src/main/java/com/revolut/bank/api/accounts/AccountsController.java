package com.revolut.bank.api.accounts;

import com.revolut.bank.api.accounts.requests.CreateAccountRequestBody;
import com.revolut.bank.api.accounts.responses.AccountDetailsResponseBody;
import com.revolut.bank.services.AccountsService;
import com.revolut.bank.services.domain.Account;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.HttpHeaders.CONTENT_TYPE;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.CREATED;
import static javax.ws.rs.core.Response.Status.OK;

@Path("/accounts")
public class AccountsController {

    private final AccountsService accountsService;

    @Inject
    public AccountsController(AccountsService accountsService) {
        this.accountsService = accountsService;
    }

    @POST
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public Response createAccount(CreateAccountRequestBody requestBody) {
        Account account = accountsService.create(requestBody.getEmail());

        return Response.status(CREATED)
                .entity(new AccountDetailsResponseBody(account))
                .header(CONTENT_TYPE, APPLICATION_JSON)
                .build();
    }

    @GET
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    @Path("/{email}/")
    public Response retrieveAccountDetails(@PathParam(value = "email") String email) {
        Account account = accountsService.retrieveDetailsOf(email);

        return Response.status(OK)
                .entity(new AccountDetailsResponseBody(account))
                .header(CONTENT_TYPE, APPLICATION_JSON)
                .build();
    }

}
