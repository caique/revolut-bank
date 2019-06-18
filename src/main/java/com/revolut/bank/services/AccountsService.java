package com.revolut.bank.services;

import com.google.common.base.Strings;
import com.revolut.bank.repositories.AccountsRepository;
import com.revolut.bank.services.domain.Account;
import com.revolut.bank.services.exceptions.MissingEmailException;

import javax.annotation.Resource;
import javax.inject.Inject;

@Resource
public class AccountsService {

    private final AccountsRepository accountsRepository;

    @Inject
    public AccountsService(AccountsRepository accountsRepository) {
        this.accountsRepository = accountsRepository;
    }

    public Account create(String email) {
        Account account = new Account(email);

        this.accountsRepository.save(account);

        return account;
    }

    public Account retrieveDetailsOf(String email) {
        if (Strings.isNullOrEmpty(email)) throw new MissingEmailException();

        return this.accountsRepository.findByEmail(email);
    }

}
