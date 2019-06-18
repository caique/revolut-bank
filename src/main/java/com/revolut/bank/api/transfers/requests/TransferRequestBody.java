package com.revolut.bank.api.transfers.requests;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.revolut.bank.services.domain.MoneyAmount;

import java.math.BigDecimal;
import java.util.Optional;

public class TransferRequestBody {

    private final Optional<String> source;
    private final Optional<String> destination;
    private final MoneyAmount amount;

    public TransferRequestBody(@JsonProperty("source") String source,
                               @JsonProperty("destination") String destination,
                               @JsonProperty("amount") BigDecimal amount) {

        this.source = Optional.ofNullable(source);
        this.destination = Optional.ofNullable(destination);
        this.amount = new MoneyAmount(amount);
    }

    public String getSource() {
        return source.orElse("");
    }

    public String getDestination() {
        return destination.orElse("");
    }

    public BigDecimal getAmount() {
        return amount.getValue();
    }

    @JsonIgnore
    public MoneyAmount getMoneyAmount() {
        return amount;
    }

}
