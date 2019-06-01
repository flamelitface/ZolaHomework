package com.zola.invoices.entities;

import javax.persistence.*;
import java.sql.Date;
import java.time.Instant;

@Entity
@Table(name = "Invoice")
public class Invoice {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "invoice_number", nullable = false)
    private String invoiceNumber;

    @Column(name = "po_number", nullable = false)
    private String poNumber;

    @Column(name = "due_date", nullable = false)
    private Date dueDate;

    @Column(name = "amount_cents", nullable = false)
    private Long amountCents;

    @Column(name = "created_at")
    private Instant created_at = Instant.now();
}