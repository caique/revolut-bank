package com.revolut.bank.api.accounts.exceptionhandlers;

import com.revolut.bank.services.exceptions.MissingEmailException;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

@Provider
public class MissingEmailExceptionMapper implements ExceptionMapper<MissingEmailException> {

    @Override
    public Response toResponse(MissingEmailException exception) {
        return Response.status(BAD_REQUEST)
                .entity(exception.getMessage())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .build();
    }

}
