package com.zola.invoices.services;

import com.zola.invoices.data.access.dao.InvoiceDAO;
import com.zola.invoices.data.access.entities.InvoiceEntity;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class InvoiceServiceTest {

    @Mock
    private InvoiceDAO invoiceDAO;

    private InvoiceService invoiceService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        invoiceService = new InvoiceService(invoiceDAO);
    }

    @Test
    public void saveInvoice_whenEntityIsPassedToInvoiceService_thenInvoiceDAOCallsSave() {
        InvoiceEntity entity = new InvoiceEntity();
        when(invoiceDAO.save(entity)).thenReturn(entity);

        assertEquals(entity, invoiceService.saveInvoice(entity));
    }

    @Test
    public void getInvoicesByInvoiceNumber_WhenCalled_CallsInvoiceDAOWithCorrectPageableValue() {
        InvoiceEntity entity = new InvoiceEntity();
        String invoiceNumber = "007";
        int limit = 20;
        int offset = 1;

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        when(invoiceDAO.findByInvoiceNumber(eq(invoiceNumber), any())).thenReturn(Arrays.asList(entity));

        Collection<InvoiceEntity> results = invoiceService.getInvoicesByInvoiceNumber(invoiceNumber, offset, limit);

        verify(invoiceDAO).findByInvoiceNumber(eq(invoiceNumber), pageableCaptor.capture());
        Pageable pageableValue = pageableCaptor.getValue();

        assertEquals(1, results.size());
        assertTrue(results.contains(entity));
        assertEquals(offset, pageableValue.getPageNumber());
        assertEquals(limit, pageableValue.getPageSize());
        assertEquals("createdAt: DESC", pageableValue.getSort().toString());
    }

    @Test
    public void getInvoicesByPoNumber_WhenCalled_CallsInvoiceDAOWithCorrectPageableValue() {
        InvoiceEntity entity = new InvoiceEntity();
        String poNumber = "poNumber007";
        int limit = 1000;
        int offset = 3;

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        when(invoiceDAO.findByPoNumber(eq(poNumber), any())).thenReturn(Arrays.asList(entity));

        Collection<InvoiceEntity> results = invoiceService.getInvoicesByPoNumber(poNumber, offset, limit);

        verify(invoiceDAO).findByPoNumber(eq(poNumber), pageableCaptor.capture());
        Pageable pageableValue = pageableCaptor.getValue();

        assertEquals(1, results.size());
        assertTrue(results.contains(entity));
        assertEquals(offset, pageableValue.getPageNumber());
        assertEquals(limit, pageableValue.getPageSize());
        assertEquals("createdAt: DESC", pageableValue.getSort().toString());
    }

}