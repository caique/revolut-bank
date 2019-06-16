package com.revolut.bank.services.accounts;

import com.revolut.bank.repositories.AccountsRepository;
import com.revolut.bank.services.accounts.domain.Account;

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

}
