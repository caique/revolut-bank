package com.revolut.bank.services;

import com.revolut.bank.repositories.AccountsRepository;
import com.revolut.bank.services.domain.Account;
import com.revolut.bank.services.domain.MoneyAmount;
import com.revolut.bank.services.exceptions.AccountNotFoundException;
import com.revolut.bank.services.exceptions.UnprocessableTransferException;

import javax.annotation.Resource;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.logging.Logger;

@Resource
public class TransfersService {

    private static final Logger logger = Logger.getLogger(TransfersService.class.getName());

    private AccountsRepository accountsRepository;

    @Inject
    public TransfersService(AccountsRepository accountsRepository) {
        this.accountsRepository = accountsRepository;
    }

    public Account transferBetweenAccounts(String source, String destination, BigDecimal amount) {
        try {
            Account sourceAccount = this.accountsRepository.findByEmail(source);
            Account destinationAccount = this.accountsRepository.findByEmail(destination);

            MoneyAmount moneyAmount = new MoneyAmount(amount);

            sourceAccount.transferTo(destinationAccount, moneyAmount);

            this.accountsRepository.update(sourceAccount);
            this.accountsRepository.update(destinationAccount);

            logger.info("A successful transfer was executed: " + source + " transferred " + amount + " to " + destination + ".");

            return sourceAccount;
        } catch (AccountNotFoundException exception) {
            throw new UnprocessableTransferException("An unexpected error occurred and the transfer was not processed.");
        }
    }

}
