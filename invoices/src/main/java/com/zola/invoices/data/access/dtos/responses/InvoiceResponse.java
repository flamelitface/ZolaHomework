package com.zola.invoices.data.access.dtos.responses;

public class InvoiceResponse {

    private Integer id;
    private String invoice_number;
    private String po_number;
    private String due_date;
    private Long amount_cents;
    private String created_at;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public String getDue_date() {
        return due_date;
    }

    public void setDue_date(String due_date) {
        this.due_date = due_date;
    }

    public Long getAmount_cents() {
        return amount_cents;
    }

    public void setAmount_cents(Long amount_cents) {
        this.amount_cents = amount_cents;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

}
