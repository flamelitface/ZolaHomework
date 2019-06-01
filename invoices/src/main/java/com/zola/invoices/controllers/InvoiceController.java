package com.zola.invoices.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zola.invoices.data.access.dtos.InvoiceCall;
import com.zola.invoices.data.access.dtos.responses.InvoiceResponse;
import com.zola.invoices.entities.InvoiceEntity;
import com.zola.invoices.services.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class InvoiceController {

    private final String INVOICE_MAPPING = "/v1";

    @Autowired
    private InvoiceService invoiceService;

    @PostMapping(path = INVOICE_MAPPING + "/invoices", consumes = "application/json", produces = "application/json")
    public String createInvoice(@RequestBody InvoiceCall invoiceCall) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        InvoiceEntity invoiceEntity = mapInvoiceCallToInvoiceEntity(invoiceCall);
        InvoiceEntity saveResult = invoiceService.saveInvoice(invoiceEntity);
        InvoiceResponse invoiceResponse = mapInvoiceEntityToInvoiceResponse(saveResult);
        return mapper.writeValueAsString(invoiceResponse);
    }

    @GetMapping(path = INVOICE_MAPPING + "/invoices", produces = "application/json")
    public String getInvoicesByInvoiceNumber(@RequestParam(name = "invoiceNumber") String invoiceNumber) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        List<InvoiceResponse> invoiceResults = invoiceService.getInvoicesByInvoiceNumber(invoiceNumber)
                .stream()
                .map(this::mapInvoiceEntityToInvoiceResponse)
                .collect(Collectors.toList());
        return mapper.writeValueAsString(invoiceResults);
    }

    //TODO: Merge the two get mapping together
    @GetMapping(path = INVOICE_MAPPING + "/invoicesByPoNumber", produces = "application/json")
    public String getInvoicesByPoNumber(@RequestParam(name = "poNumber") String poNumber) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        List<InvoiceResponse> invoiceResults = invoiceService.getInvoicesByPoNumber(poNumber)
                .stream()
                .map(this::mapInvoiceEntityToInvoiceResponse)
                .collect(Collectors.toList());
        return mapper.writeValueAsString(invoiceResults);
    }

    private InvoiceEntity mapInvoiceCallToInvoiceEntity(InvoiceCall invoiceCall) {
        InvoiceEntity entity = new InvoiceEntity();
        entity.setAmountCents(invoiceCall.getAmount_cents());
        entity.setDueDate(invoiceCall.getDue_date());
        entity.setInvoiceNumber(invoiceCall.getInvoice_number());
        entity.setPoNumber(invoiceCall.getPo_number());
        return entity;
    }

    private InvoiceResponse mapInvoiceEntityToInvoiceResponse(InvoiceEntity invoiceEntity) {
        InvoiceResponse invoiceResponse = new InvoiceResponse();
        invoiceResponse.setId(invoiceEntity.getId());
        invoiceResponse.setInvoice_number(invoiceEntity.getInvoiceNumber());
        invoiceResponse.setPo_number(invoiceEntity.getPoNumber());
        invoiceResponse.setDue_date(invoiceEntity.getDueDate().toString());
        invoiceResponse.setAmount_cents(invoiceEntity.getAmountCents());
        invoiceResponse.setCreated_at(invoiceEntity.getCreated_at().toString());
        return invoiceResponse;
    }
}
