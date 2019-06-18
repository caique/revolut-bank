package com.revolut.bank.services.exceptions;

public class UnprocessableTransferException extends RuntimeException implements GlamorousException {

    private final String message;

    public UnprocessableTransferException() {
        this.message = "An unexpected error occurred and the transfer was not processed.";
    }

    public UnprocessableTransferException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.asGlamorousMessage(this.message);
    }

}
