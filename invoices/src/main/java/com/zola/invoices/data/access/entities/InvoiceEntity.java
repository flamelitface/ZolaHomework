package com.zola.invoices.data.access.entities;

import javax.persistence.*;
import java.sql.Date;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "Invoice",
        indexes = {@Index(name = "idx_invoice_number", columnList = "invoice_number"),
                   @Index(name = "idx_po_number", columnList = "po_number")})
public class InvoiceEntity {
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
    private Instant createdAt = Instant.now().truncatedTo(ChronoUnit.SECONDS);

    public Integer getId() {
        return id;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getPoNumber() {
        return poNumber;
    }

    public void setPoNumber(String poNumber) {
        this.poNumber = poNumber;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Long getAmountCents() {
        return amountCents;
    }

    public void setAmountCents(Long amountCents) {
        this.amountCents = amountCents;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

}
