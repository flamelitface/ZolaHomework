package com.zola.invoices;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zola.invoices.data.access.dtos.calls.InvoiceCall;
import com.zola.invoices.data.access.dtos.responses.InvoiceResponse;
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

	private final String INVOICE_ENDPOINT = "/v1/invoices";

	private final String INVOICE_BY_INVOICE_NUMBER = "/v1/invoices?invoiceNumber=";

	private final String INVOICE_BY_PO_NUMBER_ENDPOINT = "/v1/invoicesByPoNumber?poNumber=";

	private final LocalDateTime startTestTime = LocalDateTime.now(ZoneId.of("UTC"));

	private final ObjectMapper mapper = new ObjectMapper();

	@Test
	public void runIntegrationTest() throws IOException {
		// Ideally the database would be cleaned up in between each test once a delete operation is implemented.
		// This way these tests wouldn't need to be run in a certain order.
		populateDatabase();
		testMultipleInvoicesCreationWithDefaults();
		testMultipleInvoicesCreationWithOffsetAndLimit();
		testSingleInvoiceCreation();
	}

	public void testMultipleInvoicesCreationWithDefaults() throws IOException {
		List<InvoiceResponse> invoiceNumberResultsWithDefaultOffsetAndLimit = getInvoice(INVOICE_BY_INVOICE_NUMBER, "staticInvoice", null, null);
		List<InvoiceResponse> poNumberResultWithDefaultOffSetAndLimit = getInvoice(INVOICE_BY_PO_NUMBER_ENDPOINT, "staticPoNumber", null, null);

		assertEquals(10, invoiceNumberResultsWithDefaultOffsetAndLimit.size());
		assertEquals(10, poNumberResultWithDefaultOffSetAndLimit.size());

		assertEquals(Integer.valueOf(100), poNumberResultWithDefaultOffSetAndLimit.get(0).getId());
		assertEquals(Integer.valueOf(91), poNumberResultWithDefaultOffSetAndLimit.get(9).getId());
		assertEquals(Integer.valueOf(300), invoiceNumberResultsWithDefaultOffsetAndLimit.get(0).getId());
		assertEquals(Integer.valueOf(291), invoiceNumberResultsWithDefaultOffsetAndLimit.get(9).getId());
	}

	public void testMultipleInvoicesCreationWithOffsetAndLimit() throws IOException {
		List<InvoiceResponse> invoiceNumberResultsWithDefaultOffsetAndLimit = getInvoice(INVOICE_BY_INVOICE_NUMBER, "staticInvoice", 1, 150);
		assertEquals(50, invoiceNumberResultsWithDefaultOffsetAndLimit.size());
		assertEquals(Integer.valueOf(150), invoiceNumberResultsWithDefaultOffsetAndLimit.get(0).getId());
		assertEquals(Integer.valueOf(101), invoiceNumberResultsWithDefaultOffsetAndLimit.get(49).getId());


		List<InvoiceResponse> poNumberResultWithDefaultOffSetAndLimit = getInvoice(INVOICE_BY_PO_NUMBER_ENDPOINT, "staticPoNumber", 5, 5);
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

		String postResult = postInvoice(invoiceCall);
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

	private List<InvoiceResponse> getInvoice(String endpoint, String number, Integer offset, Integer limit) throws IOException {
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

	private void populateDatabase() {
		for(int i = 0 ; i < 100; i++) {
			postInvoice("invoice" + i, "staticPoNumber", 12333L, LocalDate.of(2019, 1, 1));
		}

		for(int i = 0 ; i < 200; i++) {
			postInvoice("staticInvoice", "po" + i, 12333L, LocalDate.of(2019, 1, 1));
		}
	}

	private String postInvoice(String invoiceNumber, String poNumber, Long amountInCents, LocalDate localDate) {
		InvoiceCall invoiceCall = new InvoiceCall();
		invoiceCall.setInvoice_number(invoiceNumber);
		invoiceCall.setPo_number(poNumber);
		invoiceCall.setAmount_cents(amountInCents);
		invoiceCall.setDue_date(Date.valueOf(localDate));

		return postInvoice(invoiceCall);
	}

	private String postInvoice(InvoiceCall invoiceCall) {
		return restTemplate.postForObject("http://localhost:" + port + INVOICE_ENDPOINT, invoiceCall, String.class);
	}

}
