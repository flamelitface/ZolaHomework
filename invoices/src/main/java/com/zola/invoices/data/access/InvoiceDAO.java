package com.zola.invoices.data.access;

import com.zola.invoices.entities.InvoiceEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface InvoiceDAO extends PagingAndSortingRepository<InvoiceEntity, Long> {
    List<InvoiceEntity> findByInvoiceNumber(String invoiceNumber, Pageable pageable);
    List<InvoiceEntity> findByPoNumber(String poNumber, Pageable pageable);





}
