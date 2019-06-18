package com.revolut.bank.api.transfers;

import com.revolut.bank.api.transfers.requests.TransferRequestBody;
import com.revolut.bank.services.TransfersService;
import com.revolut.bank.services.domain.Account;
import com.revolut.bank.services.domain.MoneyAmount;
import com.revolut.bank.services.exceptions.UnprocessableTransferException;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.Response;
import java.math.BigDecimal;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static javax.ws.rs.core.Response.Status.OK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TransfersControllerTest {

    private TransfersController controller;
    private TransfersService transfersService;

    @Before
    public void setUp() {
        this.transfersService = mock(TransfersService.class);
        this.controller = new TransfersController(transfersService);
    }

    @Test
    public void throwsUnprocessableTransferExceptionWhenSourceIsNull() {
        when(transfersService.transferBetweenAccounts(any(), any(), any())).thenThrow(new UnprocessableTransferException());

        String source = null;
        String destination = "jane.doe@email.com";
        BigDecimal amount = BigDecimal.TEN;

        TransferRequestBody transferRequestBody = new TransferRequestBody(source, destination, amount);

        Throwable throwable = catchThrowable(() -> {
            this.controller.transfer(transferRequestBody);
        });

        assertThat(throwable).isInstanceOf(UnprocessableTransferException.class);
    }

    @Test
    public void throwsUnprocessableTransferExceptionWhenDestinationIsNull() {
        when(transfersService.transferBetweenAccounts(any(), any(), any())).thenThrow(new UnprocessableTransferException());

        String source = "john.doe@email.com";
        String destination = null;
        BigDecimal amount = BigDecimal.TEN;

        TransferRequestBody transferRequestBody = new TransferRequestBody(source, destination, amount);

        Throwable throwable = catchThrowable(() -> {
            this.controller.transfer(transferRequestBody);
        });

        assertThat(throwable).isInstanceOf(UnprocessableTransferException.class);
    }

    @Test
    public void callsCreateAccountUseCaseWhenCreatingANewAccount() {
        String source = "john.doe@email.com";
        String destination = "jane.doe@email.com";
        MoneyAmount amount = MoneyAmount.ONE;

        when(transfersService.transferBetweenAccounts(source, destination, amount))
                .thenReturn(new Account("john.doe@email.com"));

        TransferRequestBody transferRequestBody = new TransferRequestBody(source, destination, amount.getValue());

        this.controller.transfer(transferRequestBody);

        verify(transfersService).transferBetweenAccounts(source, destination, amount);
    }

    @Test
    public void returnsAccountDetailsWhenAnAccountIsCreated() {
        String source = "john.doe@email.com";
        String destination = "jane.doe@email.com";
        MoneyAmount amount = MoneyAmount.ONE;

        when(transfersService.transferBetweenAccounts(source, destination, amount))
                .thenReturn(new Account("john.doe@email.com"));

        TransferRequestBody transferRequestBody = new TransferRequestBody(source, destination, amount.getValue());

        Response response = this.controller.transfer(transferRequestBody);

        assertThat(response.getEntity()).isNotNull();
        assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
        assertThat(response.getMediaType()).isEqualTo(APPLICATION_JSON_TYPE);
    }
}
