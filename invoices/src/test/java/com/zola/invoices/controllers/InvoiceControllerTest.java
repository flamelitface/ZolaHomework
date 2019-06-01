package com.zola.invoices.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zola.invoices.data.access.dtos.calls.InvoiceCall;
import com.zola.invoices.data.access.entities.InvoiceEntity;
import com.zola.invoices.services.InvoiceService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class InvoiceControllerTest {

    @Mock
    private InvoiceService invoiceService;

    @Spy
    private ObjectMapper mapper = new ObjectMapper();

    @InjectMocks
    private InvoiceController invoiceController = new InvoiceController();

    private Long amountCents = 1L;
    private Date date = Date.valueOf(LocalDate.of(2019, 5, 30));
    private String invoiceNumber = "123";
    private String poNumber = "poNumber";

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void createInvoice_whenGivenValidInformation_callsSaveInvoiceWithCorrectInformation() throws JsonProcessingException {
        InvoiceCall invoiceCall = new InvoiceCall();
        invoiceCall.setAmount_cents(amountCents);
        invoiceCall.setDue_date(date);
        invoiceCall.setInvoice_number(invoiceNumber);
        invoiceCall.setPo_number(poNumber);

        InvoiceEntity invoiceEntity = getInvoiceEntity();

        ArgumentCaptor<InvoiceEntity> invoiceEntityCaptor = ArgumentCaptor.forClass(InvoiceEntity.class);
        when(invoiceService.saveInvoice(any())).thenReturn(invoiceEntity);

        String result = invoiceController.createInvoice(invoiceCall);

        verify(invoiceService).saveInvoice(invoiceEntityCaptor.capture());
        InvoiceEntity invoiceEntityResult = invoiceEntityCaptor.getValue();

        assertEquals(amountCents, invoiceEntityResult.getAmountCents());
        assertEquals(date, invoiceEntityResult.getDueDate());
        assertEquals(invoiceNumber, invoiceEntityResult.getInvoiceNumber());
        assertEquals(poNumber, invoiceEntityResult.getPoNumber());
        assertTrue(result.startsWith("{\"id\":null,\"invoice_number\":\"123\",\"po_number\":\"poNumber\",\"due_date\":\"2019-05-30\",\"amount_cents\":1,\"created_at\":\""));
    }

    @Test
    public void getInvoicesByInvoiceNumber() throws JsonProcessingException{
        String invoiceNumber = "testNumber";
        InvoiceEntity invoiceEntity = getInvoiceEntity();
        List<InvoiceEntity> entityResults =  Arrays.asList(invoiceEntity);
        when(invoiceService.getInvoicesByInvoiceNumber(invoiceNumber, 1, 2)).thenReturn(entityResults);

        String result = invoiceController.getInvoicesByInvoiceNumber(invoiceNumber, 1, 2);

        assertTrue(result.startsWith("[{\"id\":null,\"invoice_number\":\"123\",\"po_number\":\"poNumber\",\"due_date\":\"2019-05-30\",\"amount_cents\":1,\"created_at\""));
    }

    @Test
    public void getInvoicesByPoNumber() throws JsonProcessingException {
        String poNumber = "testNumber";
        InvoiceEntity invoiceEntity = getInvoiceEntity();
        List<InvoiceEntity> entityResults =  Arrays.asList(invoiceEntity);
        when(invoiceService.getInvoicesByPoNumber(poNumber, 1, 2)).thenReturn(entityResults);

        String result = invoiceController.getInvoicesByPoNumber(poNumber, 1, 2);

        assertTrue(result.startsWith("[{\"id\":null,\"invoice_number\":\"123\",\"po_number\":\"poNumber\",\"due_date\":\"2019-05-30\",\"amount_cents\":1,\"created_at\""));
    }

    private InvoiceEntity getInvoiceEntity() {
        InvoiceEntity invoiceEntity = new InvoiceEntity();
        invoiceEntity.setAmountCents(amountCents);
        invoiceEntity.setDueDate(date);
        invoiceEntity.setInvoiceNumber(invoiceNumber);
        invoiceEntity.setPoNumber(poNumber);
        return invoiceEntity;
    }
}