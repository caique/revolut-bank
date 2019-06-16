package com.revolut.bank.configuration;

import com.revolut.bank.services.transfers.TransfersService;
import org.glassfish.jersey.internal.inject.AbstractBinder;

public class TransfersResourcesBinder extends AbstractBinder {

    @Override
    protected void configure() {
        bind(TransfersService.class).to(TransfersService.class);
    }

}
