package com.revolut.bank.services.domain;

import org.junit.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class MoneyAmountTest {

    @Test
    public void aMoneyAmountCreatedFromNullIsNotPositive() {
        MoneyAmount moneyAmount = new MoneyAmount(null);

        assertThat(moneyAmount.isNotPositive()).isTrue();
    }

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
    public void aMoneyAmountIsAlwaysPresentedWithTwoDecimalPlacesAndRoundedHalfDown() {
        BigDecimal unformattedAmount = new BigDecimal(1.05789);

        MoneyAmount moneyAmount = new MoneyAmount(unformattedAmount);

        assertThat(moneyAmount.getAmount().toString()).isEqualTo("1.05");
    }

    @Test
    public void aMoneyAmountCreatedFromNullValueIsPresentedAsZero() {
        MoneyAmount moneyAmount = new MoneyAmount(null);

        assertThat(moneyAmount.getAmount().toString()).isEqualTo("0.00");
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
        MoneyAmount higherAmount = new MoneyAmount(BigDecimal.TEN);
        MoneyAmount lowerAmount = new MoneyAmount(BigDecimal.ONE);

        assertThat(lowerAmount.isLessThan(lowerAmount)).isFalse();
        assertThat(higherAmount.isLessThan(higherAmount)).isFalse();
        assertThat(higherAmount.isLessThan(lowerAmount)).isFalse();
        assertThat(lowerAmount.isLessThan(higherAmount)).isTrue();
    }

}
