package com.revolut.bank.configuration;

import org.glassfish.jersey.server.ResourceConfig;

public class RevolutBank extends ResourceConfig {

    public RevolutBank() {
        register(new RevolutBankBinder());
        packages(true, "com.revolut.bank");
    }

}
