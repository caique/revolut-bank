package com.revolut.bank.configuration;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

public class RevolutBank extends ResourceConfig {

    public RevolutBank() {
        register(JacksonFeature.class);
        register(new AccountsResourcesBinder());
        register(new TransfersResourcesBinder());
        packages(true, "com.revolut.bank");
    }

}
