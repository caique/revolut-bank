package com.revolut.bank.services.domain;

import org.junit.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class MoneyAmountTest {

    @Test
    public void aMoneyAmountCreatedFromZeroIsNotPositive() {
        MoneyAmount moneyAmount = new MoneyAmount(BigDecimal.ZERO);

        assertThat(moneyAmount.isNotPositive()).isTrue();
    }

    @Test
    public void aMoneyAmountCreatedFromNegativeValueIsNotPositive() {
        BigDecimal negativeAmount = new BigDecimal(-1);

        MoneyAmount moneyAmount = new MoneyAmount(negativeAmount);

        assertThat(moneyAmount.isNotPositive()).isTrue();
    }

    @Test
    public void aMoneyAmountCreatedFromPositiveValueIsPositive() {
        BigDecimal positiveAmount = BigDecimal.ONE;

        MoneyAmount moneyAmount = new MoneyAmount(positiveAmount);

        assertThat(moneyAmount.isNotPositive()).isFalse();
    }

    @Test
    public void twoMoneyAmountInstancesWithTheSameValueAreEqual() {
        BigDecimal randomAmount = new BigDecimal(1.05789);

        MoneyAmount firstMoneyAmount = new MoneyAmount(randomAmount);
        MoneyAmount secondMoneyAmount = new MoneyAmount(randomAmount);

        assertThat(firstMoneyAmount).isEqualTo(secondMoneyAmount);
    }

    @Test
    public void moneyAmountsAreComparable() {
        MoneyAmount higherAmount = new MoneyAmount(10);
        MoneyAmount lowerAmount = new MoneyAmount(1);

        assertThat(lowerAmount.isLessThan(lowerAmount)).isFalse();
        assertThat(higherAmount.isLessThan(higherAmount)).isFalse();
        assertThat(higherAmount.isLessThan(lowerAmount)).isFalse();
        assertThat(lowerAmount.isLessThan(higherAmount)).isTrue();
    }

    @Test
    public void moneyAmountCanBeAddedToAnother() {
        MoneyAmount moneyAmount = new MoneyAmount(BigDecimal.ONE);

        MoneyAmount resultedAmount = moneyAmount.add(moneyAmount);

        assertThat(resultedAmount.getValue()).isEqualTo(new BigDecimal(2));
    }

    @Test
    public void moneyAmountCanBeSubtractedFromAnother() {
        MoneyAmount moneyAmount = new MoneyAmount(BigDecimal.ONE);

        MoneyAmount resultedAmount = moneyAmount.subtract(moneyAmount);

        assertThat(resultedAmount.getValue()).isEqualTo(BigDecimal.ZERO);
    }

    @Test
    public void moneyAmountCanReturnAFormattedVersionOfValue() {
        assertThat(MoneyAmount.ZERO.getFormattedValue()).isEqualTo("0.00");
        assertThat(MoneyAmount.ONE.getFormattedValue()).isEqualTo("1.00");
        assertThat(MoneyAmount.GRAND.getFormattedValue()).isEqualTo("100.00");
    }

}
