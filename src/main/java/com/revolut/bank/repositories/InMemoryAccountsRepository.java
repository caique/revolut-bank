package com.revolut.bank.repositories;

import com.revolut.bank.services.domain.Account;
import com.revolut.bank.services.exceptions.AccountNotFoundException;
import com.revolut.bank.services.exceptions.DuplicatedAccountException;

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
        if (this.accounts.containsKey(account.getEmail())) throw new DuplicatedAccountException();

        this.accounts.put(account.getEmail(), account.getBalance());

        logger.info("A new account for the email " + account.getEmail() + " was saved.");
    }

    @Override
    public void update(Account account) throws AccountNotFoundException {
        if (this.accounts.containsKey(account.getEmail())) {
            this.accounts.put(account.getEmail(), account.getBalance());
        } else {
            throw new AccountNotFoundException();
        }
    }

    @Override
    public Account findByEmail(String email) {
        if (this.accounts.containsKey(email)) {
            BigDecimal balance = this.accounts.get(email);

            return new Account(email, balance);
        }

        throw new AccountNotFoundException();
    }

}
