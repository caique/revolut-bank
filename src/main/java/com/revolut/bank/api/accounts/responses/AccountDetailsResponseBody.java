package com.revolut.bank.api.accounts.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.revolut.bank.services.accounts.domain.Account;

import java.math.BigDecimal;

import static java.math.BigDecimal.ROUND_HALF_DOWN;

public class AccountDetailsResponseBody {

    public static final int ROUNDING_STRATEGY = ROUND_HALF_DOWN;
    public static final int NUMBER_OF_DECIMAL_PLACES = 2;

    @JsonProperty("email")
    private String email;

    @JsonProperty("balance")
    private BigDecimal balance;

    public AccountDetailsResponseBody() {
    }

    public AccountDetailsResponseBody(Account account) {
        this.email = account.getEmail();
        this.balance = account.getBalance().setScale(NUMBER_OF_DECIMAL_PLACES, ROUNDING_STRATEGY);
    }

    public String getEmail() {
        return email;
    }

    public String getBalance() {
        return balance.toString();
    }

}
