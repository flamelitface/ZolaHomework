package com.zola.invoices.data.access;

import com.zola.invoices.entities.Invoice;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface InvoiceDAO extends CrudRepository<Invoice, Long> {
    public List<Invoice> findByInvoiceNumber(String invoiceNumber);

    public List<Invoice> findByPoNumber(String poNumber);

}
