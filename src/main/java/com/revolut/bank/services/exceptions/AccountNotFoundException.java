package com.revolut.bank.services.exceptions;

public class AccountNotFoundException extends RuntimeException implements GlamorousException {

    @Override
    public String getMessage() {
        return this.asGlamorousMessage("Account does not exist.");
    }

}
