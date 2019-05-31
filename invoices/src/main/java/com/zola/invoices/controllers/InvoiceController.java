package com.zola.invoices.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zola.invoices.data.access.dtos.Invoice;
import org.springframework.web.bind.annotation.*;

@RestController
public class InvoiceController {

    private final String INVOICE_MAPPING = "/v1";

//    @RequestMapping(INVOICE_MAPPING + "/invoices")
    @PostMapping(path = INVOICE_MAPPING + "/invoices", consumes ="application/json", produces= "application/json")
    public String index(@RequestBody Invoice invoice) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

//        InvoiceResponse invoiceResponse = mapInvoiceToResponse(invoice);



        return mapper.writeValueAsString(invoice);


    }
}
