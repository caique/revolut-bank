package com.revolut.bank.api.accounts.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Optional;

public class CreateAccountRequestBody {

    private final Optional<String> email;

    public CreateAccountRequestBody(@JsonProperty("email") String email) {
        this.email = Optional.ofNullable(email);
    }

    public String getEmail() {
        return email.orElse("");
    }

}
