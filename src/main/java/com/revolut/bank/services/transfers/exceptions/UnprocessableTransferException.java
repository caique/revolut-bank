package com.revolut.bank.services.transfers.exceptions;

import com.revolut.bank.utils.exceptions.GlamorousException;

public class UnprocessableTransferException extends RuntimeException implements GlamorousException {

    private String message;

    public UnprocessableTransferException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.asGlamorousMessage(this.message);
    }

}
