Employee REST API using Dropwizard 1.0.5

Java 8 needed to run this code.

Dropwizard is an open source Java framework for developing REST APIs.


Libraries using from Dropwizard:

1. * [Jetty](http://www.eclipse.org/jetty/) for serving an HTTP server inside this project'.
2. * [Jersey](http://jersey.java.net/) for developing RESTful Web Services in Java'.
3. * [Jackson](https://github.com/FasterXML/jackson) for objects to/from JSON conversion'.

Using Hibernate features for the database.

# Running The Application

## Local Development

To test the example application run the following commands.

* To package the example run.

        mvn package

* To setup the h2 database run.

		java -jar target/DropwizardEmployee-1.0.5.jar db migrate example.yml

* To run the server run.

		java -jar target/DropwizardEmployee-1.0.5.jar server example.yml

* To post data into the application.

	curl -H "Content-Type: application/json" -X POST -d '{"firstName":"Mayur", "lastName":"Chougule","jobTitle":"Software Engineer"}' http://localhost:8080/employee

* To view the list of employees.

	http://localhost:8080/employee

## Docker Deployment

### Building the Container

```bash
mvn clean package -DskipTests
docker build -t dropwizard-employee:latest .
```

### Running with Docker

```bash
# Ephemeral storage (data lost on container restart)
docker run -p 8080:8080 -p 8081:8081 dropwizard-employee:latest

# With persistent storage
docker run -p 8080:8080 -p 8081:8081 -v $(pwd)/data:/app/data dropwizard-employee:latest
```

### Running with Docker Compose

```bash
docker-compose up --build
```

### Environment Variables

The containerized application supports the following environment variables:

- `DW_DEFAULT_NAME`: Default name for the template (default: "Stranger")
- `GRAPHITE_HOST`: Graphite server host for metrics (default: "localhost")
- `GRAPHITE_PORT`: Graphite server port (default: "2003")
- `METRICS_PREFIX`: Prefix for metrics (default: "example")

### Container Features

- **Database Persistence**: Mount `/app/data` volume for persistent H2 database storage
- **Health Checks**: Built-in health check endpoint at `http://localhost:8081/healthcheck`
- **Logging**: Container-optimized logging to stdout/stderr for log aggregation
- **Security**: Runs as non-root user for enhanced security
- **Two-stage Startup**: Automatically runs database migrations before starting the server

### Testing the Containerized Application

```bash
# Test health endpoint
curl http://localhost:8081/healthcheck

# Test employee API
curl -H "Content-Type: application/json" -X POST -d '{"firstName":"Docker", "lastName":"Test","jobTitle":"Container Engineer"}' http://localhost:8080/employee

# View employees
curl http://localhost:8080/employee
```
