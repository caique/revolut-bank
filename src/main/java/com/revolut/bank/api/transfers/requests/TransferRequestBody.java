package com.revolut.bank.api.transfers.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.Optional;

import static java.math.BigDecimal.ZERO;

public class TransferRequestBody {

    private final Optional<String> source;
    private final Optional<String> destination;
    private final Optional<BigDecimal> amount;

    public TransferRequestBody(@JsonProperty("source") String source,
                               @JsonProperty("destination") String destination,
                               @JsonProperty("amount") BigDecimal amount) {

        this.source = Optional.ofNullable(source);
        this.destination = Optional.ofNullable(destination);
        this.amount = Optional.ofNullable(amount);
    }

    public String getSource() {
        return source.orElse("");
    }

    public String getDestination() {
        return destination.orElse("");
    }

    public BigDecimal getAmount() {
        return amount.orElse(ZERO);
    }

}
