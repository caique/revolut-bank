package com.revolut.bank.services.accounts.domain;

import com.google.common.base.Strings;
import com.revolut.bank.services.accounts.exceptions.MissingEmailException;

import java.math.BigDecimal;

public class Account {

    private final String email;
    private final BigDecimal balance;

    public Account(String email) {

        if(Strings.isNullOrEmpty(email)) throw new MissingEmailException();

        this.email = email;
        this.balance = new BigDecimal(100.00);
    }

    public String getEmail() {
        return this.email;
    }

    public BigDecimal getBalance() {
        return this.balance;
    }

}
