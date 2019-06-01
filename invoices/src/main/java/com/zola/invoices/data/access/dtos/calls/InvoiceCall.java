package com.zola.invoices.data.access.dtos.calls;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Date;

public class InvoiceCall {

    private String invoice_number;
    private String po_number;
    private Date due_date;
    private Long amount_cents;

    public String getInvoice_number() {
        return invoice_number;
    }

    public void setInvoice_number(String invoice_number) {
        this.invoice_number = invoice_number;
    }
    public String getPo_number() {
        return po_number;
    }

    public void setPo_number(String po_number) {
        this.po_number = po_number;
    }

    public Date getDue_date() {
        return due_date;
    }

    @JsonFormat(shape= JsonFormat.Shape.STRING, timezone="EST")
    public void setDue_date(Date due_date) {
        this.due_date = due_date;
    }

    public Long getAmount_cents() {
        return amount_cents;
    }

    public void setAmount_cents(Long amount_cents) {
        this.amount_cents = amount_cents;
    }

}
