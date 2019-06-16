package com.revolut.bank.services;

import com.revolut.bank.repositories.AccountsRepository;
import com.revolut.bank.services.domain.Account;
import com.revolut.bank.services.domain.MoneyAmount;
import com.revolut.bank.services.exceptions.AccountNotFoundException;
import com.revolut.bank.services.exceptions.UnprocessableTransferException;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TransfersServiceTest {

    private TransfersService transfersService;
    private AccountsRepository accountsRepository;

    @Before
    public void setUp() {
        this.accountsRepository = mock(AccountsRepository.class);
        this.transfersService = new TransfersService(accountsRepository);
    }

    @Test
    public void throwsUnprocessableTransferExceptionWhenTransferingWithANullSource() {
        String source = null;
        String destination = "jane.doe@email.com";
        BigDecimal amount = BigDecimal.TEN;

        when(accountsRepository.findByEmail(source)).thenReturn(null);
        when(accountsRepository.findByEmail(destination)).thenReturn(new Account(destination));

        Throwable throwable = catchThrowable(() -> {
            this.transfersService.transferBetweenAccounts(source, destination, amount);
        });

        assertThat(throwable).isInstanceOf(UnprocessableTransferException.class);
        assertThat(throwable.getMessage()).isEqualTo("{\"error\":\"Source is required to transfer money between accounts.\"}");
    }

    @Test
    public void throwsUnprocessableTransferExceptionWhenTransferingWithAnEmptySource() {
        String source = "";
        String destination = "jane.doe@email.com";
        BigDecimal amount = BigDecimal.TEN;

        when(accountsRepository.findByEmail(source)).thenReturn(null);
        when(accountsRepository.findByEmail(destination)).thenReturn(new Account(destination));

        Throwable throwable = catchThrowable(() -> {
            this.transfersService.transferBetweenAccounts(source, destination, amount);
        });

        assertThat(throwable).isInstanceOf(UnprocessableTransferException.class);
        assertThat(throwable.getMessage()).isEqualTo("{\"error\":\"Source is required to transfer money between accounts.\"}");
    }

    @Test
    public void throwsUnprocessableTransferExceptionWhenTransferingWithANullDestination() {
        String source = "john.doe@email.com";
        String destination = null;
        BigDecimal amount = BigDecimal.TEN;

        when(accountsRepository.findByEmail(source)).thenReturn(new Account(source));
        when(accountsRepository.findByEmail(destination)).thenReturn(null);

        Throwable throwable = catchThrowable(() -> {
            this.transfersService.transferBetweenAccounts(source, destination, amount);
        });

        assertThat(throwable).isInstanceOf(UnprocessableTransferException.class);
        assertThat(throwable.getMessage()).isEqualTo("{\"error\":\"Destination is required to transfer money between accounts.\"}");
    }

    @Test
    public void throwsUnprocessableTransferExceptionWhenTransferingWithAnEmptyDestination() {
        String source = "john.doe@email.com";
        String destination = "";
        BigDecimal amount = BigDecimal.TEN;

        when(accountsRepository.findByEmail(source)).thenReturn(new Account(source));
        when(accountsRepository.findByEmail(destination)).thenReturn(null);

        Throwable throwable = catchThrowable(() -> {
            this.transfersService.transferBetweenAccounts(source, destination, amount);
        });

        assertThat(throwable).isInstanceOf(UnprocessableTransferException.class);
        assertThat(throwable.getMessage()).isEqualTo("{\"error\":\"Destination is required to transfer money between accounts.\"}");
    }

    @Test
    public void throwsUnprocessableTransferExceptionWhenTransferingWithAmountZero() {
        String source = "john.doe@email.com";
        String destination = "jane.doe@email.com";
        BigDecimal amount = BigDecimal.ZERO;

        when(accountsRepository.findByEmail(source)).thenReturn(new Account(source));
        when(accountsRepository.findByEmail(destination)).thenReturn(new Account(destination));

        Throwable throwable = catchThrowable(() -> {
            this.transfersService.transferBetweenAccounts(source, destination, amount);
        });

        assertThat(throwable).isInstanceOf(UnprocessableTransferException.class);
        assertThat(throwable.getMessage()).isEqualTo("{\"error\":\"Amount to be transferred must be higher than zero.\"}");
    }

    @Test
    public void throwsUnprocessableTransferExceptionWhenSourceAndDestinationAreTheSameAccount() throws AccountNotFoundException {
        String email = "john.doe@email.com";
        BigDecimal amount = BigDecimal.TEN;

        Account singleAccount = spy(new Account(email, amount));
        MoneyAmount moneyAmount = new MoneyAmount(amount);

        when(accountsRepository.findByEmail(email)).thenReturn(singleAccount);

        Throwable throwable = catchThrowable(() -> {
            this.transfersService.transferBetweenAccounts(email, email, amount);
        });

        assertThat(throwable).isInstanceOf(UnprocessableTransferException.class);
        assertThat(throwable.getMessage()).isEqualTo("{\"error\":\"Only transfers between different accounts are possible.\"}");
    }

    @Test
    public void callsAccountRepositoryToRetrieveAccounts() {
        String source = "john.doe@email.com";
        String destination = "jane.doe@email.com";
        BigDecimal amount = BigDecimal.TEN;

        when(accountsRepository.findByEmail(source)).thenReturn(new Account(source));
        when(accountsRepository.findByEmail(destination)).thenReturn(new Account(destination));

        this.transfersService.transferBetweenAccounts(source, destination, amount);

        verify(accountsRepository).findByEmail(source);
        verify(accountsRepository).findByEmail(destination);
    }

    @Test
    public void processTransferBetweenAccountsAndUpdateBoth() throws AccountNotFoundException {
        String source = "john.doe@email.com";
        String destination = "jane.doe@email.com";
        BigDecimal amount = BigDecimal.TEN;

        Account sourceAccount = spy(new Account(source, amount));
        Account destinationAccount = spy(new Account(destination, amount));
        MoneyAmount moneyAmount = new MoneyAmount(amount);

        when(accountsRepository.findByEmail(source)).thenReturn(sourceAccount);
        when(accountsRepository.findByEmail(destination)).thenReturn(destinationAccount);

        Account account = this.transfersService.transferBetweenAccounts(source, destination, amount);

        assertThat(account).isEqualTo(sourceAccount);

        verify(sourceAccount).transferTo(destinationAccount, moneyAmount);
        verify(accountsRepository).update(sourceAccount);
        verify(accountsRepository).update(destinationAccount);

        assertThat(sourceAccount.getBalance().toString()).isEqualTo("0.00");
        assertThat(destinationAccount.getBalance().toString()).isEqualTo("20.00");
    }

}
