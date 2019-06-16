package com.revolut.bank.services.accounts.exceptions;

import com.revolut.bank.utils.exceptions.GlamorousException;

public class MissingEmailException extends RuntimeException implements GlamorousException {

    @Override
    public String getMessage() {
        return this.asGlamorousMessage("Email is required when creating an account");
    }

}
