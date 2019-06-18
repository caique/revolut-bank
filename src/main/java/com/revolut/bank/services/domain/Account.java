package com.revolut.bank.services.domain;

import com.revolut.bank.services.exceptions.MissingEmailException;
import com.revolut.bank.services.exceptions.UnprocessableTransferException;

import java.util.Objects;

public class Account {

    private final String email;
    private MoneyAmount balance;

    public Account(String email) {
        if (Objects.isNull(email) || email.isEmpty()) throw new MissingEmailException();

        this.balance = MoneyAmount.GRAND;
        this.email = email;
    }

    public Account(String email, MoneyAmount balance) {
        this.email = email;
        this.balance = balance;
    }

    public String getEmail() {
        return this.email;
    }

    public MoneyAmount getBalance() {
        return this.balance;
    }

    public void transferTo(Account destinationAccount, MoneyAmount amount) {
        if (destinationAccount.equals(this)) throw new UnprocessableTransferException("Only transfers between different accounts are possible.");
        if (Objects.isNull(amount)) throw new UnprocessableTransferException("Amount to be transferred must not be null.");
        if (amount.isNotPositive()) throw new UnprocessableTransferException("Amount to be transferred must be higher than zero.");
        if (this.balance.isLessThan(amount)) throw new UnprocessableTransferException("Insufficient funds.");

        this.subtractFromBalance(amount);
        destinationAccount.addToBalance(amount);
    }

    private void addToBalance(MoneyAmount amount) {
        this.balance = this.balance.add(amount);
    }

    private void subtractFromBalance(MoneyAmount amount) {
        this.balance = this.balance.subtract(amount);
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
