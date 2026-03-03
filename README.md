# Employee REST API using Dropwizard 1.0.5

A simple REST API to add and list employees, built with the [Dropwizard](http://www.dropwizard.io/) Java framework.

## Prerequisites

- **Java 8** (JDK 1.8) or later
- **Apache Maven 3.x**

## Technology Stack

- [Dropwizard 1.0.5](http://www.dropwizard.io/) - REST framework
- [Jetty](http://www.eclipse.org/jetty/) - Embedded HTTP server
- [Jersey](https://jersey.github.io/) - JAX-RS reference implementation for RESTful web services
- [Jackson](https://github.com/FasterXML/jackson) - JSON serialization/deserialization
- [Hibernate](http://hibernate.org/) - ORM for database access
- [H2 Database](http://www.h2database.com/) - In-memory/file-based database

## Java 8 Features Used

This project has been upgraded to target Java 8 and takes advantage of the following features:

- **`java.util.Optional`** - Used in DAO and Template classes for null-safe value handling
- **`java.util.Objects`** - Used for equals/hashCode implementations
- **`java.util.StringJoiner`** - Used for readable `toString()` implementations
- **JUnit 4 annotations** - Modern test style with `@Test`, `@Before` annotations

## Setup and Running

### 1. Build the project

```bash
mvn clean package
```

### 2. Set up the H2 database

```bash
java -jar target/DropwizardEmployee-1.0.5.jar db migrate example.yml
```

### 3. Start the server

```bash
java -jar target/DropwizardEmployee-1.0.5.jar server example.yml
```

The application will start on port **8080** (application) and port **8081** (admin).

## API Endpoints

### Create an Employee

```bash
curl -H "Content-Type: application/json" -X POST \
  -d '{"firstName":"Mayur", "lastName":"Chougule","jobTitle":"Software Engineer"}' \
  http://localhost:8080/employee
```

### List All Employees

```
GET http://localhost:8080/employee
```

Open [http://localhost:8080/employee](http://localhost:8080/employee) in your browser or use curl:

```bash
curl http://localhost:8080/employee
```

## Running Tests

```bash
mvn test
```

## Configuration

The application is configured via `example.yml`. Key settings include:

- **Database**: H2 file-based database at `./target/example`
- **Server**: HTTP on port 8080, admin on port 8081
- **Logging**: Console and file appender at `/tmp/application.log`
