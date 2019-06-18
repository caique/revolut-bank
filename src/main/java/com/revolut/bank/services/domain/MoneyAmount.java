package com.revolut.bank.services.domain;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

import static java.math.BigDecimal.ROUND_FLOOR;

public class MoneyAmount {

    private static final int NUMBER_OF_DECIMAL_PLACES = 2;
    private static final int ROUNDING_STRATEGY = ROUND_FLOOR;

    public static final MoneyAmount ZERO = new MoneyAmount(BigDecimal.ZERO);
    public static final MoneyAmount ONE = new MoneyAmount(BigDecimal.ONE);
    public static final MoneyAmount GRAND = new MoneyAmount(new BigDecimal(100));

    private final Optional<BigDecimal> amount;

    public MoneyAmount(BigDecimal amount) {
        this.amount = Optional.ofNullable(amount);
    }

    public MoneyAmount(Integer amount) {
        this(new BigDecimal(amount));
    }

    public MoneyAmount(Double amount) {
        this(new BigDecimal(amount));
    }

    public BigDecimal getValue() {
        return this.amount.orElse(BigDecimal.ZERO);
    }

    public Boolean isNotPositive() {
        return this.getValue().compareTo(BigDecimal.ZERO) <= 0;
    }

    public Boolean isLessThan(MoneyAmount otherAmount) {
        BigDecimal value = this.getValue();
        BigDecimal otherValue = otherAmount.getValue();

        return value.compareTo(otherValue) < 0;
    }

    public MoneyAmount add(MoneyAmount otherAmount) {
        BigDecimal resultedAmount = this.getValue().add(otherAmount.getValue());

        return new MoneyAmount(resultedAmount);
    }

    public MoneyAmount subtract(MoneyAmount otherAmount) {
        BigDecimal resultedAmount = this.getValue().subtract(otherAmount.getValue());

        return new MoneyAmount(resultedAmount);
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

    public BigDecimal getFormattedValue() {
        return this.getValue().setScale(NUMBER_OF_DECIMAL_PLACES, ROUNDING_STRATEGY);
    }

}
