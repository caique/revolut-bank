package com.revolut.bank.services.accounts.exceptions;

import com.revolut.bank.utils.exceptions.GlamorousException;

public class DuplicatedAccountException extends RuntimeException implements GlamorousException {

    @Override
    public String getMessage() {
        return this.asGlamorousMessage("An account already exists with this email.");
    }

}
