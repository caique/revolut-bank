package com.revolut.bank.api.accounts.exceptionhandlers;

import com.revolut.bank.services.accounts.exceptions.DuplicatedAccountException;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import static javax.ws.rs.core.Response.Status.CONFLICT;

@Provider
public class DuplicatedAccountExceptionMapper implements ExceptionMapper<DuplicatedAccountException> {

    @Override
    public Response toResponse(DuplicatedAccountException exception) {
        return Response.status(CONFLICT)
                .entity(exception.getMessage())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .build();
    }

}
