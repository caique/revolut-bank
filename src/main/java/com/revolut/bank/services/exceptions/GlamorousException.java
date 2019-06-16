package com.revolut.bank.services.exceptions;

import org.json.JSONObject;

public interface GlamorousException {

    default String asGlamorousMessage(String message) {
        return new JSONObject()
                .put("error", message)
                .toString();
    }

}
