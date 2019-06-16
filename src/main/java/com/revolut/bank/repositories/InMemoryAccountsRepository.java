package com.revolut.bank.repositories;

import com.revolut.bank.services.accounts.domain.Account;
import com.revolut.bank.services.accounts.exceptions.DuplicatedAccountException;

import javax.annotation.Resource;
import javax.inject.Singleton;
import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

@Singleton
@Resource
public class InMemoryAccountsRepository implements AccountsRepository {

    private static final Logger logger = Logger.getLogger(InMemoryAccountsRepository.class.getName());

    private Map<String, BigDecimal> accounts;

    public InMemoryAccountsRepository() {
        this.accounts = new ConcurrentHashMap<>();
    }

    @Override
    public void save(Account account) {
        if(this.accounts.containsKey(account.getEmail())) throw new DuplicatedAccountException();

        this.accounts.put(account.getEmail(), account.getBalance());

        logger.info("A new account for the email " + account.getEmail() + " was saved.");
    }

}
