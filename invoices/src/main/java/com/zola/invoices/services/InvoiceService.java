package com.zola.invoices.services;

import com.zola.invoices.data.access.InvoiceDAO;
import com.zola.invoices.entities.InvoiceEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InvoiceService {

    private InvoiceDAO invoiceDAO;

    @Autowired
    public InvoiceService(InvoiceDAO invoiceDAO) {
        this.invoiceDAO = invoiceDAO;
    }

    public InvoiceEntity saveInvoice(InvoiceEntity invoiceEntity) {
        return invoiceDAO.save(invoiceEntity);
    }

    public List<InvoiceEntity> getInvoicesByInvoiceNumber(String invoiceNumber) {
        List<InvoiceEntity> results = invoiceDAO.findByInvoiceNumber(invoiceNumber);
        return results;
    }

    public List<InvoiceEntity> getInvoicesByPoNumber(String poNumber) {
        List<InvoiceEntity> results = invoiceDAO.findByPoNumber(poNumber);
        return results;
    }

}
