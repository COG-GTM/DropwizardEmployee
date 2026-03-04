# DropwizardEmployee

Employee REST API using Dropwizard 1.0.5 and **Java 8**.

Dropwizard is an open source Java framework for developing REST APIs.

## Prerequisites

- **Java 8** (JDK 1.8) or later
- **Apache Maven** 3.x

## Libraries

This project uses the following Dropwizard-bundled libraries:

1. [Jetty](http://www.eclipse.org/jetty/) - Embedded HTTP server
2. [Jersey](http://jersey.java.net/) - RESTful Web Services framework
3. [Jackson](https://github.com/FasterXML/jackson) - JSON serialization/deserialization
4. [Hibernate](http://hibernate.org/) - ORM for database access (H2 embedded database)

## Java 8 Features Used

This project leverages the following Java 8 features:

- **`java.util.Optional`** - Used in DAO and Template classes for null-safe value handling
- **`java.util.StringJoiner`** - Used in Employee's `toString()` for readable string representation
- **`java.util.Objects`** - Used for null-safe `equals()` and `hashCode()` implementations
- **JUnit 4 annotations** - Modern test annotations (`@Test`, `@Before`) replacing JUnit 3 style

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

The application will be available at `http://localhost:8080`.
The admin interface will be available at `http://localhost:8081`.

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
