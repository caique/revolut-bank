package com.revolut.bank.api.transfers.exceptionhandlers;

import com.revolut.bank.services.accounts.exceptions.DuplicatedAccountException;
import com.revolut.bank.services.transfers.exceptions.UnprocessableTransferException;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class UnprocessableTransferExceptionMapper implements ExceptionMapper<UnprocessableTransferException> {

    public static final int UNPROCESSABLE_ENTITY = 422;

    @Override
    public Response toResponse(UnprocessableTransferException exception) {
        return Response.status(UNPROCESSABLE_ENTITY)
                .entity(exception.getMessage())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .build();
    }

}
