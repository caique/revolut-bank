package com.revolut.bank.services.exceptions;

public class DuplicatedAccountException extends RuntimeException implements GlamorousException {

    @Override
    public String getMessage() {
        return this.asGlamorousMessage("An account already exists with this email.");
    }

}
