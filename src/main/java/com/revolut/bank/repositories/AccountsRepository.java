package com.revolut.bank.repositories;

import com.revolut.bank.services.accounts.domain.Account;

public interface AccountsRepository {

    void save(Account account);

}
