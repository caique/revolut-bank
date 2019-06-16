package com.revolut.bank.services.accounts.exceptions;

import com.revolut.bank.utils.exceptions.GlamorousException;

public class AccountNotFoundException extends Exception implements GlamorousException {

    @Override
    public String getMessage() {
        return this.asGlamorousMessage("Account does not exist.");
    }

}
