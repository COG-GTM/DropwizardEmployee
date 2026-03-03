# Employee REST API using Dropwizard 1.0.5

A RESTful Employee management API built with the Dropwizard framework, upgraded to **Java 8**.

## Prerequisites

- **Java 8** (JDK 1.8) or higher
- **Apache Maven 3.x**

## Technology Stack

- [Dropwizard 1.0.5](http://www.dropwizard.io/) - RESTful web framework
- [Jetty](http://www.eclipse.org/jetty/) - Embedded HTTP server
- [Jersey](http://jersey.java.net/) - JAX-RS reference implementation for RESTful web services
- [Jackson](https://github.com/FasterXML/jackson) - JSON serialization/deserialization
- [Hibernate](http://hibernate.org/) - ORM for database operations
- [H2 Database](http://www.h2database.com/) - In-memory database for development

## Java 8 Features Used

This project leverages the following Java 8 features:

- **Lambda expressions** - Used in stream operations and `Optional.map()` for concise code
- **Stream API** - Used for filtering employees by job title (`stream().filter().collect()`)
- **Optional** - Used for null-safe employee lookups (`Optional.ofNullable`, `Optional.map`, `orElse`)
- **Method references** - Used where applicable for improved readability
- **JUnit 4 annotations** - Tests modernized from JUnit 3 `TestCase` style to JUnit 4 `@Test` annotations

## Setup and Running

### 1. Build the project

```bash
mvn clean package
```

### 2. Run the tests

```bash
mvn test
```

### 3. Set up the H2 database

```bash
java -jar target/DropwizardEmployee-1.0.5.jar db migrate example.yml
```

### 4. Start the server

```bash
java -jar target/DropwizardEmployee-1.0.5.jar server example.yml
```

The application will start on port **8080** (application) and port **8081** (admin).

## API Endpoints

### Create an employee

```bash
curl -H "Content-Type: application/json" -X POST \
  -d '{"firstName":"Mayur", "lastName":"Chougule","jobTitle":"Software Engineer"}' \
  http://localhost:8080/employee
```

### List all employees

```
GET http://localhost:8080/employee
```

### Get employee by ID

```
GET http://localhost:8080/employee/{id}
```

Returns `404 Not Found` if the employee does not exist.

### Filter employees by job title

```
GET http://localhost:8080/employee/jobtitle/{jobTitle}
```

Case-insensitive filtering using Java 8 Streams.

## Configuration

The application is configured via `example.yml`. Key settings include:

- **Database**: H2 in-memory database (`jdbc:h2:./target/example`)
- **Server ports**: 8080 (application), 8081 (admin)
- **Logging**: Console and file logging to `/tmp/application.log`

## Project Structure

```
src/
  main/java/com/dropwizard/employee/
    EmployeeApplication.java    - Application entry point and bundle configuration
    EmployeeConfiguration.java  - Configuration POJO
    core/
      Employee.java             - JPA entity with equals, hashCode, toString
      Template.java             - Template rendering with Optional support
    db/
      EmployeeDAO.java          - Data access layer using Hibernate with Optional
    resources/
      EmployeeResource.java     - REST endpoints with Java 8 Optional and Streams
  test/java/com/dropwizard/employee/
    AppTest.java                - Basic application test (JUnit 4)
    core/
      EmployeeTest.java         - Employee entity unit tests
      TemplateTest.java         - Template unit tests with Optional
    resources/
      EmployeeResourceTest.java - Resource tests with Mockito
```
