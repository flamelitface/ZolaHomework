package com.zola.invoices.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zola.invoices.data.access.dtos.calls.InvoiceCall;
import com.zola.invoices.data.access.dtos.responses.InvoiceResponse;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class InvoiceAPIUtil {

    private final static String INVOICE_ENDPOINT = "/v1/invoices";

    private RestTemplate restTemplate;

    private TestRestTemplate testRestTemplate;

    private final int port;

    private ObjectMapper mapper;

    private final IllegalStateException illegalStateException = new IllegalStateException("InvoiceAPIUtil has not been initialized with a valid restTemplate object");

    public InvoiceAPIUtil(RestTemplate restTemplate, int port, ObjectMapper mapper) {
        this.restTemplate = restTemplate;
        this.testRestTemplate = null;
        this.port = port;
        this.mapper = mapper;
    }

    public InvoiceAPIUtil(TestRestTemplate testRestTemplate, int port, ObjectMapper mapper) {
        this.testRestTemplate = testRestTemplate;
        this.restTemplate = null;
        this.port = port;
        this.mapper = mapper;
    }

    public List<InvoiceResponse> getInvoice(String endpoint, String number, Integer offset, Integer limit) throws IOException {
        StringBuilder builder = new StringBuilder(number);
        if(offset != null) {
            builder.append("&offset=" + offset);
        }
        if(limit != null) {
            builder.append("&limit=" + limit);
        }
        String endpointQueryString = builder.toString();

        String result;
        if(restTemplate != null) {
            result = restTemplate.getForObject("http://localhost:" + port + endpoint + endpointQueryString, String.class);
        } else if(testRestTemplate!= null) {
            result = testRestTemplate.getForObject("http://localhost:" + port + endpoint + endpointQueryString, String.class);
        } else {
            throw illegalStateException;
        }
        return mapper.readValue(result, new TypeReference<List<InvoiceResponse>>(){});
    }

    public void populateDatabase(int numberOfInvoicesOfFirstVariation, int numberOfInvoicesOfSecondVariation) {
        for (int i = 0; i < numberOfInvoicesOfFirstVariation; i++) {
            postInvoice("invoice" + i, "staticPoNumber", 12333L, LocalDate.of(2019, 1, 1));
        }

        for (int i = 0; i < numberOfInvoicesOfSecondVariation; i++) {
            postInvoice("staticInvoice", "po" + i, 12333L, LocalDate.of(2019, 1, 1));
        }
    }

    public String postInvoice(String invoiceNumber, String poNumber, Long amountInCents, LocalDate localDate) {
        InvoiceCall invoiceCall = new InvoiceCall();
        invoiceCall.setInvoice_number(invoiceNumber);
        invoiceCall.setPo_number(poNumber);
        invoiceCall.setAmount_cents(amountInCents);
        invoiceCall.setDue_date(Date.valueOf(localDate));
        return postInvoice(invoiceCall);
    }

    public String postInvoice(InvoiceCall invoiceCall) {
        if (restTemplate != null) {
            return restTemplate.postForObject("http://localhost:" + port + INVOICE_ENDPOINT, invoiceCall, String.class);
        } else {
            if (testRestTemplate != null) {
                return testRestTemplate.postForObject("http://localhost:" + port + INVOICE_ENDPOINT, invoiceCall, String.class);
            }
        }
        throw illegalStateException;
    }


}
