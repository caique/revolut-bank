package com.revolut.bank.services;

import com.revolut.bank.repositories.AccountsRepository;
import com.revolut.bank.services.domain.Account;
import com.revolut.bank.services.domain.MoneyAmount;
import com.revolut.bank.services.exceptions.AccountNotFoundException;
import com.revolut.bank.services.exceptions.UnprocessableTransferException;

import javax.annotation.Resource;
import javax.inject.Inject;
import java.util.logging.Logger;

@Resource
public class TransfersService {

    private static final Logger logger = Logger.getLogger(TransfersService.class.getName());

    private AccountsRepository accountsRepository;

    @Inject
    public TransfersService(AccountsRepository accountsRepository) {
        this.accountsRepository = accountsRepository;
    }

    public Account transferBetweenAccounts(String source, String destination, MoneyAmount amount) {
        try {
            Account sourceAccount = this.accountsRepository.findByEmail(source);
            Account destinationAccount = this.accountsRepository.findByEmail(destination);

            sourceAccount.transferTo(destinationAccount, amount);

            this.accountsRepository.update(sourceAccount);
            this.accountsRepository.update(destinationAccount);

            logger.info("A successful transfer was executed: "
                    + source + " transferred " + amount .getFormattedValue() + " to " + destination + ".");

            return sourceAccount;

        } catch (AccountNotFoundException exception) {
            throw new UnprocessableTransferException();
        }
    }

}
