package com.dropwizard.employee.resources;

import com.dropwizard.employee.core.Employee;
import com.dropwizard.employee.db.EmployeeDAO;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the EmployeeResource REST resource.
 * Uses Mockito to mock the DAO layer and validates Java 8 Optional/Stream behavior.
 */
public class EmployeeResourceTest {

    private EmployeeDAO employeeDAO;
    private EmployeeResource resource;

    @Before
    public void setUp() {
        employeeDAO = mock(EmployeeDAO.class);
        resource = new EmployeeResource(employeeDAO);
    }

    @Test
    public void testCreateEmployee() {
        Employee employee = new Employee("John", "Doe", "Software Engineer");
        when(employeeDAO.create(employee)).thenReturn(employee);

        Employee result = resource.createEmployee(employee);

        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        verify(employeeDAO).create(employee);
    }

    @Test
    public void testListEmployee() {
        List<Employee> employees = Arrays.asList(
                new Employee("John", "Doe", "Software Engineer"),
                new Employee("Jane", "Smith", "Product Manager")
        );
        when(employeeDAO.findAll()).thenReturn(employees);

        List<Employee> result = resource.listEmployee();

        assertEquals(2, result.size());
        assertEquals("John", result.get(0).getFirstName());
        assertEquals("Jane", result.get(1).getFirstName());
    }

    @Test
    public void testListEmployeeEmpty() {
        when(employeeDAO.findAll()).thenReturn(Collections.emptyList());

        List<Employee> result = resource.listEmployee();

        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testGetEmployeeFound() {
        Employee employee = new Employee("John", "Doe", "Software Engineer");
        employee.setId(1L);
        when(employeeDAO.findById(1L)).thenReturn(Optional.of(employee));

        Response response = resource.getEmployee(1L);

        assertEquals(200, response.getStatus());
        assertEquals(employee, response.getEntity());
    }

    @Test
    public void testGetEmployeeNotFound() {
        when(employeeDAO.findById(999L)).thenReturn(Optional.empty());

        Response response = resource.getEmployee(999L);

        assertEquals(404, response.getStatus());
    }

    @Test
    public void testGetEmployeesByJobTitle() {
        List<Employee> employees = Arrays.asList(
                new Employee("John", "Doe", "Software Engineer"),
                new Employee("Jane", "Smith", "Product Manager"),
                new Employee("Bob", "Jones", "Software Engineer")
        );
        when(employeeDAO.findAll()).thenReturn(employees);

        List<Employee> result = resource.getEmployeesByJobTitle("Software Engineer");

        assertEquals(2, result.size());
        assertEquals("John", result.get(0).getFirstName());
        assertEquals("Bob", result.get(1).getFirstName());
    }

    @Test
    public void testGetEmployeesByJobTitleCaseInsensitive() {
        List<Employee> employees = Arrays.asList(
                new Employee("John", "Doe", "Software Engineer"),
                new Employee("Jane", "Smith", "Product Manager")
        );
        when(employeeDAO.findAll()).thenReturn(employees);

        List<Employee> result = resource.getEmployeesByJobTitle("software engineer");

        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getFirstName());
    }

    @Test
    public void testGetEmployeesByJobTitleNoMatch() {
        List<Employee> employees = Arrays.asList(
                new Employee("John", "Doe", "Software Engineer")
        );
        when(employeeDAO.findAll()).thenReturn(employees);

        List<Employee> result = resource.getEmployeesByJobTitle("CEO");

        assertEquals(0, result.size());
    }
}
