package com.revolut.bank.api.transfers.requests;

import com.revolut.bank.services.domain.MoneyAmount;
import org.junit.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class TransferRequestBodyTest {

    @Test
    public void returnsEmptyStringWhenSourceIsNull() {
        String source = null;
        String destination = "jane.doe@email.com";
        BigDecimal amount = BigDecimal.TEN;

        TransferRequestBody transferRequestBody = new TransferRequestBody(source, destination, amount);

        assertThat(transferRequestBody.getSource()).isEqualTo("");
    }

    @Test
    public void returnsProperValueWhenSourceIsNotNull() {
        String source = "john.doe@email.com";
        String destination = "jane.doe@email.com";
        BigDecimal amount = BigDecimal.TEN;

        TransferRequestBody transferRequestBody = new TransferRequestBody(source, destination, amount);

        assertThat(transferRequestBody.getSource()).isEqualTo(source);
    }

    @Test
    public void returnsEmptyStringWhenDestinationIsNull() {
        String source = "john.doe@email.com";
        String destination = null;
        BigDecimal amount = BigDecimal.TEN;

        TransferRequestBody transferRequestBody = new TransferRequestBody(source, destination, amount);

        assertThat(transferRequestBody.getDestination()).isEqualTo("");
    }

    @Test
    public void returnsProperValueWhenDestinationIsNotNull() {
        String source = "john.doe@email.com";
        String destination = "jane.doe@email.com";
        BigDecimal amount = BigDecimal.TEN;

        TransferRequestBody transferRequestBody = new TransferRequestBody(source, destination, amount);

        assertThat(transferRequestBody.getDestination()).isEqualTo(destination);
    }

    @Test
    public void returnsZeroWhenAmountIsNull() {
        String source = "john.doe@email.com";
        String destination = "jane.doe@email.com";
        BigDecimal amount = null;

        TransferRequestBody transferRequestBody = new TransferRequestBody(source, destination, amount);

        assertThat(transferRequestBody.getAmount()).isEqualTo(BigDecimal.ZERO);
    }

    @Test
    public void returnsProperValueWhenAmountIsNotNull() {
        String source = "john.doe@email.com";
        String destination = "jane.doe@email.com";
        BigDecimal amount = BigDecimal.TEN;

        TransferRequestBody transferRequestBody = new TransferRequestBody(source, destination, amount);

        assertThat(transferRequestBody.getAmount()).isEqualTo(amount);
    }

    @Test
    public void returnsTheProperMoneyAmountInstance() {
        String source = "john.doe@email.com";
        String destination = "jane.doe@email.com";
        BigDecimal amount = BigDecimal.ONE;

        TransferRequestBody transferRequestBody = new TransferRequestBody(source, destination, amount);

        assertThat(transferRequestBody.getMoneyAmount()).isEqualTo(MoneyAmount.ONE);
    }

}
