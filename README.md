Employee REST API using Dropwizard 1.0.5

Java 8 needed to run this code.

Dropwizard is an open source Java framework for developing REST APIs.


Libraries using from Dropwizard:

1. * [Jetty](http://www.eclipse.org/jetty/) for serving an HTTP server inside this project'.
2. * [Jersey](http://jersey.java.net/) for developing RESTful Web Services in Java'.
3. * [Jackson](https://github.com/FasterXML/jackson) for objects to/from JSON conversion'.

Using Hibernate features for the database.

# Running The Application

To test the example application run the following commands.

* To package the example run.

        mvn package

* To setup the h2 database run.

		java -jar target/DropwizardEmployee-1.0.5.jar db migrate example.yml

* To run the server run. The `/employee` API requires HTTP Basic authentication; the server will not
  start until `EMPLOYEE_API_PASSWORD` (at least 12 characters) is set. `EMPLOYEE_API_USERNAME` defaults to `admin`.
  Users and roles (`READER`, `WRITER`) are configured in the `auth` block of `example.yml`.

		export EMPLOYEE_API_PASSWORD='<choose-a-strong-password>'
		java -jar target/DropwizardEmployee-1.0.5.jar server example.yml

* To post data into the application (requires the `WRITER` role).

	curl -u "admin:$EMPLOYEE_API_PASSWORD" -H "Content-Type: application/json" -X POST -d '{"firstName":"Mayur", "lastName":"Chougule","jobTitle":"Software Engineer"}' http://localhost:8080/employee

* To view the list of employees (requires the `READER` or `WRITER` role).

	curl -u "admin:$EMPLOYEE_API_PASSWORD" http://localhost:8080/employee

  Requests without valid credentials get `401 Unauthorized`; authenticated users without the required role get `403 Forbidden`.
  Basic credentials are only base64-encoded, so expose the API over HTTPS (see the commented `https` connector in `example.yml`) or behind a TLS-terminating proxy.