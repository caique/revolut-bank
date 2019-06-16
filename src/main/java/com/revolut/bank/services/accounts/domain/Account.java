package com.revolut.bank.services.accounts.domain;

import com.google.common.base.Strings;
import com.revolut.bank.services.accounts.exceptions.MissingEmailException;
import com.revolut.bank.services.transfers.domain.MoneyAmount;
import com.revolut.bank.services.transfers.exceptions.UnprocessableTransferException;

import java.math.BigDecimal;
import java.util.Objects;

public class Account {

    private final String email;
    private MoneyAmount balance;

    public Account(String email, BigDecimal balance) {
        this.email = email;
        this.balance = new MoneyAmount(balance);
    }

    public Account(String email) {
        if (Strings.isNullOrEmpty(email)) throw new MissingEmailException();

        BigDecimal arbitraryInitialAmount = new BigDecimal(100.00);
        this.balance = new MoneyAmount(arbitraryInitialAmount);
        this.email = email;
    }

    public String getEmail() {
        return this.email;
    }

    public BigDecimal getBalance() {
        return this.balance.getAmount();
    }

    public void transferTo(Account destinationAccount, MoneyAmount amount) {
        if (destinationAccount.equals(this)) throw new UnprocessableTransferException("Only transfers between different accounts are possible.");
        if (Objects.isNull(amount)) throw new UnprocessableTransferException("Amount to be transferred must not be null.");
        if (amount.isNotPositive()) throw new UnprocessableTransferException("Amount to be transferred must be higher than zero.");
        if (this.balance.isLessThan(amount)) throw new UnprocessableTransferException("Insufficient funds.");

        BigDecimal valueToBeTransferred = amount.getAmount();

        this.subtractFromBalance(valueToBeTransferred);
        destinationAccount.addToBalance(valueToBeTransferred);
    }

    private void addToBalance(BigDecimal amount) {
        BigDecimal newBalance = this.getBalance().add(amount);
        this.balance = new MoneyAmount(newBalance);
    }

    private void subtractFromBalance(BigDecimal amount) {
        BigDecimal newBalance = this.getBalance().subtract(amount);
        this.balance = new MoneyAmount(newBalance);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof Account)) return false;

        Account account = (Account) other;

        return Objects.equals(email, account.email) &&
                Objects.equals(balance, account.balance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, balance);
    }

}
