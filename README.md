# Employee REST API using Dropwizard 1.0.5

## Prerequisites

- **Java 8** (JDK 1.8) or higher is required to build and run this application.
- **Apache Maven 3.x** for building the project.

## Overview

This is a REST API for managing employees, built with [Dropwizard](http://www.dropwizard.io/) 1.0.5 and compiled with **Java 8**.

### Libraries Used

- [Jetty](http://www.eclipse.org/jetty/) — Embedded HTTP server
- [Jersey](http://jersey.java.net/) — RESTful Web Services framework
- [Jackson](https://github.com/FasterXML/jackson) — JSON serialization/deserialization
- [Hibernate](http://hibernate.org/) — ORM for database operations (H2 in-memory database)

### Java 8 Features Used

- **`Optional`** for null-safe entity lookups in `EmployeeDAO`
- **Lambda expressions** and **Streams API** for filtering and searching employees in `EmployeeResource`
- **`StringJoiner`** for the `Employee.toString()` implementation
- **Method references** and functional-style programming throughout the codebase
- **JUnit 4 annotations** (`@Test`, `@Before`) replacing legacy JUnit 3 `TestCase` style

## Setup and Running the Application

1. **Build the project:**

        mvn clean package

2. **Run the tests:**

        mvn test

3. **Set up the H2 database:**

        java -jar target/DropwizardEmployee-1.0.5.jar db migrate example.yml

4. **Start the server:**

        java -jar target/DropwizardEmployee-1.0.5.jar server example.yml

   The application will start on port **8080** (application) and **8081** (admin).

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

### Get Employee by ID

```
GET http://localhost:8080/employee/{id}
```

### Search Employees by Job Title

```
GET http://localhost:8080/employee/search?jobTitle=Engineer
```

## Project Structure

```
src/
├── main/java/com/dropwizard/employee/
│   ├── EmployeeApplication.java    # Main Dropwizard application entry point
│   ├── EmployeeConfiguration.java  # Application configuration
│   ├── core/
│   │   ├── Employee.java           # Employee entity (JPA)
│   │   └── Template.java           # Template helper using Optional
│   ├── db/
│   │   └── EmployeeDAO.java        # Data Access Object with Optional support
│   └── resources/
│       └── EmployeeResource.java   # REST endpoints with Streams API
└── test/java/com/dropwizard/employee/
    ├── AppTest.java                # Basic app test (JUnit 4)
    └── core/
        ├── EmployeeTest.java       # Employee unit tests with streams/lambdas
        └── TemplateTest.java       # Template unit tests with Optional
```
