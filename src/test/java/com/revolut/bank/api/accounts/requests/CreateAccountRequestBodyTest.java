package com.revolut.bank.api.accounts.requests;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CreateAccountRequestBodyTest {

    @Test
    public void returnsEmptyStringWhenEmailIsNull() {
        CreateAccountRequestBody createAccountRequestBody = new CreateAccountRequestBody(null);

        assertThat(createAccountRequestBody.getEmail()).isEqualTo("");
    }

    @Test
    public void returnsProperValueWhenEmailIsNotNull() {
        CreateAccountRequestBody createAccountRequestBody = new CreateAccountRequestBody("any not null value");

        assertThat(createAccountRequestBody.getEmail()).isEqualTo("any not null value");
    }

}
