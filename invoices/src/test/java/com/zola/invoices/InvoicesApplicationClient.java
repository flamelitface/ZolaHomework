package com.zola.invoices;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zola.invoices.util.InvoiceAPIUtil;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InvoicesApplicationClient {

	private static int port = 8080;

	private static RestTemplate restTemplate = new RestTemplate();

	private final static String INVOICE_BY_INVOICE_NUMBER = "/v1/invoices?invoiceNumber=";

	private final static String INVOICE_BY_PO_NUMBER_ENDPOINT = "/v1/invoicesByPoNumber?poNumber=";

	private final static ObjectMapper mapper = new ObjectMapper();

	public static void main(String[] args) throws IOException {
		Logger logger = Logger.getLogger("System Logger");
		InvoiceAPIUtil invoiceApiUtil = new InvoiceAPIUtil(restTemplate, port, mapper);

		// Uncomment this if you want to generate some data
//		invoiceApiUtil.populateDatabase(10000, 50000);

		// Uncomment this and modify as you like to create an invoice
//		String postResult = invoiceApiUtil.postInvoice("invoiceNumber", "poNumber", 12333L, LocalDate.of(2019, 1, 1));
//		logger.log(Level.INFO, postResult);

		// Uncomment this to search for invoices by invoice number. You can set limit and offset here (optional).
//		String getResultByInvoiceNumber = mapper.writeValueAsString(invoiceApiUtil.getInvoice(INVOICE_BY_INVOICE_NUMBER, "staticInvoice", 0, 10000));
//		logger.log(Level.INFO, getResultByInvoiceNumber);

		// Uncomment this to search for invoices by poNumber. You can set limit and offset here (optional).
//		String getResultByPoNumber = mapper.writeValueAsString(invoiceApiUtil.getInvoice(INVOICE_BY_PO_NUMBER_ENDPOINT, "staticPoNumber", null, null));
//		logger.log(Level.INFO, getResultByPoNumber);
	}
}
