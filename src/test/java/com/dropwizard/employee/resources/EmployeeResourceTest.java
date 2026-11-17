package com.dropwizard.employee.resources;

import com.dropwizard.employee.core.Employee;
import com.dropwizard.employee.db.EmployeeDAO;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import io.dropwizard.testing.junit5.ResourceExtension;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

@ExtendWith(DropwizardExtensionsSupport.class)
public class EmployeeResourceTest {

    private static final EmployeeDAO DAO = mock(EmployeeDAO.class);

    private static final ResourceExtension RESOURCES = ResourceExtension.builder()
            .addResource(new EmployeeResource(DAO))
            .build();

    @AfterEach
    public void tearDown() {
        reset(DAO);
    }

    @Test
    public void listsEmployees() {
        Employee ada = employee(1, "Ada", "Lovelace", "Engineer");
        Employee alan = employee(2, "Alan", "Turing", "Analyst");
        when(DAO.findAll()).thenReturn(Arrays.asList(ada, alan));

        List<Employee> employees = RESOURCES.target("/employee").request()
                .get(new GenericType<List<Employee>>() { });

        assertEquals(Arrays.asList(ada, alan), employees);
    }

    @Test
    public void createsEmployee() {
        Employee saved = employee(7, "Grace", "Hopper", "Admiral");
        when(DAO.create(any(Employee.class))).thenReturn(saved);

        Response response = RESOURCES.target("/employee").request()
                .post(Entity.entity(new Employee("Grace", "Hopper", "Admiral"), MediaType.APPLICATION_JSON));

        assertEquals(200, response.getStatus());
        assertEquals(saved, response.readEntity(Employee.class));
    }

    private static Employee employee(long id, String first, String last, String title) {
        Employee e = new Employee(first, last, title);
        e.setId(id);
        return e;
    }
}
