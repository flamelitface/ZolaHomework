#Invoice Service

This is a Spring Boot service with an integrated H2 database. 

You can run the application by running the InvoicesApplication class. For example, Intellij users can right click 
class and select "Run 'InvoicesApplication''".

Once it is running, you can interact with it by running the InvoicesApplicationClient as a separate process. 
There are some methods you can modify and uncomment to perform interactions with the running Invoice Service.

Alternatively, you can use a REST client such as postman or a simple cURL. Here are some examples:


This posts an invoice into the service

curl -X POST \
  http://localhost:8080/v1/invoices \
  -H 'Cache-Control: no-cache' \
  -H 'Content-Type: application/json' \
  -d '{
	"invoice_number": "ABC12348",
	"po_number": "X1B23C4D5H",
	"due_date": "2017-03-15",
	"amount_cents": 100000
}'

This will retrieve all invoices matching the specified Invoice Number (with offset set to 0 and limit set to 100)

curl -X GET \
  'http://localhost:8080/v1/invoices?invoiceNumber=ABC12348&offset=0&limit=100' \
  -H 'Cache-Control: no-cache' \
  -H 'Postman-Token: 15ae1d30-4633-4acf-a8af-f4114b93b297'

This will retrieve all invoices matching the specified Po Number

curl -X GET \
  'http://localhost:8080/v1/invoicesByPoNumber?poNumber=X1B23C4D5H' \
  -H 'Cache-Control: no-cache' \
  -H 'Postman-Token: b03bf4b5-3401-4a4c-877b-9c58d9cdf3ba'

Offset will default to 0 and limit will default to 10 if not otherwise specified.



