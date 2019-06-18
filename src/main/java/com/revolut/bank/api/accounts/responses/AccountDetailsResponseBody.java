package com.revolut.bank.api.accounts.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.revolut.bank.services.domain.Account;
import com.revolut.bank.services.domain.MoneyAmount;

import java.math.BigDecimal;

import static java.math.BigDecimal.ROUND_FLOOR;
import static java.math.BigDecimal.ROUND_HALF_DOWN;

public class AccountDetailsResponseBody {

    public static final int ROUNDING_STRATEGY = ROUND_FLOOR;
    public static final int NUMBER_OF_DECIMAL_PLACES = 2;

    @JsonProperty("email")
    private String email;

    @JsonProperty("balance")
    private MoneyAmount balance;

    public AccountDetailsResponseBody() {
    }

    public AccountDetailsResponseBody(Account account) {
        this.email = account.getEmail();
        this.balance = account.getBalance();
    }

    public String getEmail() {
        return this.email;
    }

    public BigDecimal getBalance() {
        return this.balance.getFormattedValue();
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = new MoneyAmount(balance);
    }

}
