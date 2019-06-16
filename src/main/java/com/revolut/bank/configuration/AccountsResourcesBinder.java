package com.revolut.bank.configuration;

import com.revolut.bank.repositories.AccountsRepository;
import com.revolut.bank.repositories.InMemoryAccountsRepository;
import com.revolut.bank.services.accounts.AccountsService;
import org.glassfish.jersey.internal.inject.AbstractBinder;

public class AccountsResourcesBinder extends AbstractBinder {

    @Override
    protected void configure() {
        bind(AccountsService.class).to(AccountsService.class);
        bind(new InMemoryAccountsRepository()).to(AccountsRepository.class);
    }

}
