package com.zola.invoices.data.access;

import com.zola.invoices.entities.InvoiceEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface InvoiceDAO extends CrudRepository<InvoiceEntity, Long> {
    public List<InvoiceEntity> findByInvoiceNumber(String invoiceNumber);

    public List<InvoiceEntity> findByPoNumber(String poNumber);

}
