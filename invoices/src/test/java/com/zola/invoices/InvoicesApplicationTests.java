package com.zola.invoices;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zola.invoices.data.access.dtos.calls.InvoiceCall;
import com.zola.invoices.data.access.dtos.responses.InvoiceResponse;
import com.zola.invoices.util.InvoiceAPIUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class InvoicesApplicationTests {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	private final String INVOICE_BY_INVOICE_NUMBER = "/v1/invoices?invoiceNumber=";

	private final String INVOICE_BY_PO_NUMBER_ENDPOINT = "/v1/invoicesByPoNumber?poNumber=";

	private final LocalDateTime startTestTime = LocalDateTime.now(ZoneId.of("UTC"));

	private final ObjectMapper mapper = new ObjectMapper();

	private InvoiceAPIUtil invoiceApiUtil = new InvoiceAPIUtil(restTemplate, port, mapper);

	@Before
	public void init() {
		invoiceApiUtil = new InvoiceAPIUtil(restTemplate, port, mapper);
	}

	@Test
	public void runIntegrationTest() throws IOException {
		// Ideally the database would be cleaned up in between each test once a delete operation is implemented.
		// This way these tests wouldn't need to be run in a certain order.
		invoiceApiUtil.populateDatabase(100, 200);
		testMultipleInvoicesCreationWithDefaults();
		testMultipleInvoicesCreationWithOffsetAndLimit();
		testSingleInvoiceCreation();
	}

	public void testMultipleInvoicesCreationWithDefaults() throws IOException {
		List<InvoiceResponse> invoiceNumberResultsWithDefaultOffsetAndLimit = invoiceApiUtil.getInvoice(INVOICE_BY_INVOICE_NUMBER, "staticInvoice", null, null);
		List<InvoiceResponse> poNumberResultWithDefaultOffSetAndLimit = invoiceApiUtil.getInvoice(INVOICE_BY_PO_NUMBER_ENDPOINT, "staticPoNumber", null, null);

		assertEquals(10, invoiceNumberResultsWithDefaultOffsetAndLimit.size());
		assertEquals(10, poNumberResultWithDefaultOffSetAndLimit.size());

		assertEquals(Integer.valueOf(100), poNumberResultWithDefaultOffSetAndLimit.get(0).getId());
		assertEquals(Integer.valueOf(91), poNumberResultWithDefaultOffSetAndLimit.get(9).getId());
		assertEquals(Integer.valueOf(300), invoiceNumberResultsWithDefaultOffsetAndLimit.get(0).getId());
		assertEquals(Integer.valueOf(291), invoiceNumberResultsWithDefaultOffsetAndLimit.get(9).getId());
	}

	public void testMultipleInvoicesCreationWithOffsetAndLimit() throws IOException {
		List<InvoiceResponse> invoiceNumberResultsWithDefaultOffsetAndLimit = invoiceApiUtil.getInvoice(INVOICE_BY_INVOICE_NUMBER, "staticInvoice", 1, 150);
		assertEquals(50, invoiceNumberResultsWithDefaultOffsetAndLimit.size());
		assertEquals(Integer.valueOf(150), invoiceNumberResultsWithDefaultOffsetAndLimit.get(0).getId());
		assertEquals(Integer.valueOf(101), invoiceNumberResultsWithDefaultOffsetAndLimit.get(49).getId());


		List<InvoiceResponse> poNumberResultWithDefaultOffSetAndLimit = invoiceApiUtil.getInvoice(INVOICE_BY_PO_NUMBER_ENDPOINT, "staticPoNumber", 5, 5);
		assertEquals(5, poNumberResultWithDefaultOffSetAndLimit.size());
		assertEquals(Integer.valueOf(75), poNumberResultWithDefaultOffSetAndLimit.get(0).getId());
		assertEquals(Integer.valueOf(71), poNumberResultWithDefaultOffSetAndLimit.get(4).getId());

	}

	public void testSingleInvoiceCreation() throws IOException {
		InvoiceCall invoiceCall = new InvoiceCall();
		invoiceCall.setInvoice_number("inv123");
		invoiceCall.setPo_number("po123");
		invoiceCall.setAmount_cents(12333L);
		invoiceCall.setDue_date(Date.valueOf("2019-03-12"));

		ObjectMapper mapper = new ObjectMapper();

		String postResult = invoiceApiUtil.postInvoice(invoiceCall);
		InvoiceResponse response = mapper.readValue(postResult, InvoiceResponse.class);

		assertEquals(Integer.valueOf(301), response.getId());
		assertEquals("inv123", response.getInvoice_number());
		assertEquals("po123", response.getPo_number());
		assertEquals("2019-03-12", response.getDue_date());
		assertEquals(Long.valueOf("12333"), response.getAmount_cents());

		final LocalDateTime endTestTime = LocalDateTime.now(ZoneId.of("UTC"));
		LocalDateTime creationTime = LocalDateTime.parse(response.getCreated_at(), DateTimeFormatter.ISO_DATE_TIME);
		assertTrue("Creation time is after test start time", creationTime.isAfter(startTestTime));
		assertTrue("Creation time is before test end time", creationTime.isBefore(endTestTime));
	}
}
