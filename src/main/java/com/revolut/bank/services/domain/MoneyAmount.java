package com.revolut.bank.services.domain;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

public class MoneyAmount {

    public static final int ROUNDING_STRATEGY = BigDecimal.ROUND_FLOOR;
    public static final int NUMBER_OF_DECIMAL_PLACES = 2;

    private final Optional<BigDecimal> amount;

    public MoneyAmount(BigDecimal amount) {
        this.amount = Optional.ofNullable(amount);
    }

    public Boolean isNotPositive() {
        return this.getAmount().compareTo(BigDecimal.ZERO) <= 0;
    }

    public Boolean isLessThan(MoneyAmount otherAmount) {
        return this.getAmount().compareTo(otherAmount.getAmount()) < 0;
    }

    public BigDecimal getAmount() {
        return this.amount.orElse(BigDecimal.ZERO).setScale(NUMBER_OF_DECIMAL_PLACES, ROUNDING_STRATEGY);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof MoneyAmount)) return false;

        MoneyAmount that = (MoneyAmount) other;

        return Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount);
    }

}
