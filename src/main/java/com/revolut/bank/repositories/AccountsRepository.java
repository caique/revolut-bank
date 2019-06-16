package com.revolut.bank.repositories;

import com.revolut.bank.services.accounts.domain.Account;
import com.revolut.bank.services.accounts.exceptions.AccountNotFoundException;

public interface AccountsRepository {

    void save(Account account);

    void update(Account account) throws AccountNotFoundException;

    Account findByEmail(String email);

}
