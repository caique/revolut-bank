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

        when(accountsRepository.findByEmail(source)).thenThrow(new AccountNotFoundException());
        when(accountsRepository.findByEmail(destination)).thenReturn(new Account(destination));

        Throwable throwable = catchThrowable(() -> {
            this.transfersService.transferBetweenAccounts(source, destination, MoneyAmount.ONE);
        });

        assertThat(throwable).isInstanceOf(UnprocessableTransferException.class);
        assertThat(throwable.getMessage()).isEqualTo("{\"error\":\"An unexpected error occurred and the transfer was not processed.\"}");
    }

    @Test
    public void throwsUnprocessableTransferExceptionWhenTransferingWithAnEmptySource() {
        String source = "";
        String destination = "jane.doe@email.com";

        when(accountsRepository.findByEmail(source)).thenThrow(new AccountNotFoundException());
        when(accountsRepository.findByEmail(destination)).thenReturn(new Account(destination));

        Throwable throwable = catchThrowable(() -> {
            this.transfersService.transferBetweenAccounts(source, destination, MoneyAmount.ONE);
        });

        assertThat(throwable).isInstanceOf(UnprocessableTransferException.class);
        assertThat(throwable.getMessage()).isEqualTo("{\"error\":\"An unexpected error occurred and the transfer was not processed.\"}");
    }

    @Test
    public void throwsUnprocessableTransferExceptionWhenTransferingWithANullDestination() {
        String source = "john.doe@email.com";
        String destination = null;

        when(accountsRepository.findByEmail(source)).thenReturn(new Account(source));
        when(accountsRepository.findByEmail(destination)).thenThrow(new AccountNotFoundException());

        Throwable throwable = catchThrowable(() -> {
            this.transfersService.transferBetweenAccounts(source, destination, MoneyAmount.ONE);
        });

        assertThat(throwable).isInstanceOf(UnprocessableTransferException.class);
        assertThat(throwable.getMessage()).isEqualTo("{\"error\":\"An unexpected error occurred and the transfer was not processed.\"}");
    }

    @Test
    public void throwsUnprocessableTransferExceptionWhenTransferingWithAnEmptyDestination() {
        String source = "john.doe@email.com";
        String destination = "";

        when(accountsRepository.findByEmail(source)).thenReturn(new Account(source));
        when(accountsRepository.findByEmail(destination)).thenThrow(new AccountNotFoundException());

        Throwable throwable = catchThrowable(() -> {
            this.transfersService.transferBetweenAccounts(source, destination, MoneyAmount.ONE);
        });

        assertThat(throwable).isInstanceOf(UnprocessableTransferException.class);
        assertThat(throwable.getMessage()).isEqualTo("{\"error\":\"An unexpected error occurred and the transfer was not processed.\"}");
    }

    @Test
    public void throwsUnprocessableTransferExceptionWhenTransferingWithAmountZero() {
        String source = "john.doe@email.com";
        String destination = "jane.doe@email.com";

        when(accountsRepository.findByEmail(source)).thenReturn(new Account(source));
        when(accountsRepository.findByEmail(destination)).thenReturn(new Account(destination));

        Throwable throwable = catchThrowable(() -> {
            this.transfersService.transferBetweenAccounts(source, destination, MoneyAmount.ZERO);
        });

        assertThat(throwable).isInstanceOf(UnprocessableTransferException.class);
        assertThat(throwable.getMessage()).isEqualTo("{\"error\":\"Amount to be transferred must be higher than zero.\"}");
    }

    @Test
    public void throwsUnprocessableTransferExceptionWhenSourceAndDestinationAreTheSameAccount() {
        String email = "john.doe@email.com";

        Account singleAccount = spy(new Account(email, MoneyAmount.GRAND));

        when(accountsRepository.findByEmail(email)).thenReturn(singleAccount);

        Throwable throwable = catchThrowable(() -> {
            this.transfersService.transferBetweenAccounts(email, email, MoneyAmount.ONE);
        });

        assertThat(throwable).isInstanceOf(UnprocessableTransferException.class);
        assertThat(throwable.getMessage()).isEqualTo("{\"error\":\"Only transfers between different accounts are possible.\"}");
    }

    @Test
    public void callsAccountRepositoryToRetrieveAccounts() {
        String source = "john.doe@email.com";
        String destination = "jane.doe@email.com";

        when(accountsRepository.findByEmail(source)).thenReturn(new Account(source));
        when(accountsRepository.findByEmail(destination)).thenReturn(new Account(destination));

        this.transfersService.transferBetweenAccounts(source, destination, MoneyAmount.ONE);

        verify(accountsRepository).findByEmail(source);
        verify(accountsRepository).findByEmail(destination);
    }

    @Test
    public void processTransferBetweenAccountsAndUpdateBoth() {
        String source = "john.doe@email.com";
        String destination = "jane.doe@email.com";

        Account sourceAccount = spy(new Account(source, MoneyAmount.ONE));
        Account destinationAccount = spy(new Account(destination, MoneyAmount.ONE));

        when(accountsRepository.findByEmail(source)).thenReturn(sourceAccount);
        when(accountsRepository.findByEmail(destination)).thenReturn(destinationAccount);

        Account account = this.transfersService.transferBetweenAccounts(source, destination, MoneyAmount.ONE);

        assertThat(account).isEqualTo(sourceAccount);

        verify(sourceAccount).transferTo(destinationAccount, MoneyAmount.ONE);
        verify(accountsRepository).update(sourceAccount);
        verify(accountsRepository).update(destinationAccount);

        assertThat(sourceAccount.getBalance()).isEqualTo(MoneyAmount.ZERO);
        assertThat(destinationAccount.getBalance()).isEqualTo(new MoneyAmount(2));
    }

}
