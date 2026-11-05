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

* To run the server run. The `/employee` API requires HTTP Basic authentication and is served over HTTPS on port 8443.
  The server will not start until `EMPLOYEE_API_PASSWORD` (at least 12 characters), `EMPLOYEE_KEYSTORE_PATH` and
  `EMPLOYEE_KEYSTORE_PASSWORD` are set. `EMPLOYEE_API_USERNAME` defaults to `admin`. Users and roles (`READER`, `WRITER`)
  are configured in the `auth` block of `example.yml`. For local development you can create a self-signed keystore
  (do not commit it; the bundled `example.keystore` certificate expired in 2025 and is rejected by Jetty):

		keytool -genkeypair -alias localhost -keyalg RSA -keysize 2048 -validity 365 -dname CN=localhost \
		  -keystore dev.keystore -storepass '<keystore-password>' -keypass '<keystore-password>'
  Plain HTTP on port 8080 only listens on `127.0.0.1` (override with `EMPLOYEE_HTTP_BIND_HOST` only behind a TLS-terminating proxy).

		export EMPLOYEE_API_PASSWORD='<choose-a-strong-password>'
		export EMPLOYEE_KEYSTORE_PATH=dev.keystore EMPLOYEE_KEYSTORE_PASSWORD='<keystore-password>'
		java -jar target/DropwizardEmployee-1.0.5.jar server example.yml

* To post data into the application (requires the `WRITER` role). `-k` is only needed for a self-signed keystore.

	curl -k -u "admin:$EMPLOYEE_API_PASSWORD" -H "Content-Type: application/json" -X POST -d '{"firstName":"Mayur", "lastName":"Chougule","jobTitle":"Software Engineer"}' https://localhost:8443/employee

* To view the list of employees (requires the `READER` or `WRITER` role).

	curl -k -u "admin:$EMPLOYEE_API_PASSWORD" https://localhost:8443/employee

  Requests without valid credentials get `401 Unauthorized`; authenticated users without the required role get `403 Forbidden`.
