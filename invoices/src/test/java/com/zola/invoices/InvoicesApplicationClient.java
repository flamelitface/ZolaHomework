package com.zola.invoices;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zola.invoices.data.access.dtos.calls.InvoiceCall;
import com.zola.invoices.data.access.dtos.responses.InvoiceResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertTrue;

public class InvoicesApplicationClient {

	private static int port = 8080;

	private static RestTemplate restTemplate = new RestTemplate();

	private final static String INVOICE_ENDPOINT = "/v1/invoices";

	private final static String INVOICE_BY_INVOICE_NUMBER = "/v1/invoices?invoiceNumber=";

	private final static String INVOICE_BY_PO_NUMBER_ENDPOINT = "/v1/invoicesByPoNumber?poNumber=";

	private final static LocalDateTime startTestTime = LocalDateTime.now(ZoneId.of("UTC"));

	private final static ObjectMapper mapper = new ObjectMapper();

	public static void main(String[] args) throws IOException {
		Logger logger = Logger.getLogger("System Logger");

		//Uncomment this if you want to generate some data
		//populateDatabase();

		// Uncomment this and modify as you like to create an invoice
		//String postResult = postInvoice("invoiceNumber", "poNumber", 12333L, LocalDate.of(2019, 1, 1));
		//logger.log(Level.INFO, postResult);

		// Uncomment this to search for invoices by invoice number. You can set limit and offset here.
		//String getResultByInvoiceNumber = mapper.writeValueAsString(getInvoice(INVOICE_BY_INVOICE_NUMBER, "invoiceNumber", null, null));
		//logger.log(Level.INFO, getResultByInvoiceNumber);

		//Uncomment this to search for invoices by poNumber. You can set limit and offset here.
		//String getResultByPoNumber = mapper.writeValueAsString(getInvoice(INVOICE_BY_PO_NUMBER_ENDPOINT, "poNumber", null, null));
		//logger.log(Level.INFO, getResultByPoNumber);
	}

	private static List<InvoiceResponse> getInvoice(String endpoint, String number, Integer offset, Integer limit) throws IOException {
		StringBuilder builder = new StringBuilder(number);

		if(offset != null) {
			builder.append("&offset=" + offset);
		}
		if(limit != null) {
			builder.append("&limit=" + limit);
		}

		String endpointQueryString = builder.toString();

		String result = restTemplate.getForObject("http://localhost:" + port + endpoint + endpointQueryString, String.class);
		return mapper.readValue(result, new TypeReference<List<InvoiceResponse>>(){});
	}

	private static void populateDatabase() {
		for(int i = 0 ; i < 100; i++) {
			postInvoice("invoice" + i, "staticPoNumber", 12333L, LocalDate.of(2019, 1, 1));
		}

		for(int i = 0 ; i < 200; i++) {
			postInvoice("staticInvoice", "po" + i, 12333L, LocalDate.of(2019, 1, 1));
		}
	}

	private static String postInvoice(String invoiceNumber, String poNumber, Long amountInCents, LocalDate localDate) {
		InvoiceCall invoiceCall = new InvoiceCall();
		invoiceCall.setInvoice_number(invoiceNumber);
		invoiceCall.setPo_number(poNumber);
		invoiceCall.setAmount_cents(amountInCents);
		invoiceCall.setDue_date(Date.valueOf(localDate));

		return postInvoice(invoiceCall);
	}

	private static String postInvoice(InvoiceCall invoiceCall) {
		return restTemplate.postForObject("http://localhost:" + port + INVOICE_ENDPOINT, invoiceCall, String.class);
	}

}
