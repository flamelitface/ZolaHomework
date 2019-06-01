package com.zola.invoices.services;

import com.zola.invoices.data.access.InvoiceDAO;
import com.zola.invoices.entities.InvoiceEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
}
