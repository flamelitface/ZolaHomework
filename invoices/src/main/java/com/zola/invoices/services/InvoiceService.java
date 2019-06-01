package com.zola.invoices.services;

import com.zola.invoices.data.access.InvoiceDAO;
import com.zola.invoices.entities.InvoiceEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InvoiceService {

    private InvoiceDAO invoiceDAO;
    private final String CREATED_AT_COLUMN = "createdAt";

    @Autowired
    public InvoiceService(InvoiceDAO invoiceDAO) {
        this.invoiceDAO = invoiceDAO;
    }

    public InvoiceEntity saveInvoice(InvoiceEntity invoiceEntity) {
        return invoiceDAO.save(invoiceEntity);
    }

    public List<InvoiceEntity> getInvoicesByInvoiceNumber(String invoiceNumber, int offset, int limit) {
        Pageable pageable = getPageableSortedByCreationTime(offset, limit);
        List<InvoiceEntity> results = invoiceDAO.findByInvoiceNumber(invoiceNumber, pageable);
        return results;
    }

    public List<InvoiceEntity> getInvoicesByPoNumber(String poNumber, int offset, int limit) {
        Pageable pageable = getPageableSortedByCreationTime(offset, limit);
        List<InvoiceEntity> results = invoiceDAO.findByPoNumber(poNumber, pageable);
        return results;
    }

    private Pageable getPageableSortedByCreationTime(int offset, int limit) {
        return PageRequest.of(offset,limit, Sort.by(CREATED_AT_COLUMN).descending());
    }

}
