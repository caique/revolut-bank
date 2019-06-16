package com.revolut.bank.services.exceptions;

public class MissingEmailException extends RuntimeException implements GlamorousException {

    @Override
    public String getMessage() {
        return this.asGlamorousMessage("Email is required when creating an account");
    }

}
