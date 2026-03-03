package com.dropwizard.employee.resources;

import com.dropwizard.employee.core.Employee;
import com.dropwizard.employee.db.EmployeeDAO;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the {@link EmployeeResource} REST resource.
 * Uses Mockito to mock the DAO layer and verify resource behavior.
 */
public class EmployeeResourceTest {

    private EmployeeDAO employeeDAO;
    private EmployeeResource resource;

    @Before
    public void setUp() {
        employeeDAO = Mockito.mock(EmployeeDAO.class);
        resource = new EmployeeResource(employeeDAO);
    }

    @Test
    public void testCreateEmployee() {
        Employee input = new Employee("John", "Doe", "Engineer");
        Employee persisted = new Employee("John", "Doe", "Engineer");
        persisted.setId(1L);

        when(employeeDAO.create(input)).thenReturn(persisted);

        Employee result = resource.createEmployee(input);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("John", result.getFirstName());
        verify(employeeDAO).create(input);
    }

    @Test
    public void testListEmployeesReturnsAll() {
        List<Employee> employees = Arrays.asList(
                new Employee("John", "Doe", "Engineer"),
                new Employee("Jane", "Smith", "Manager")
        );
        when(employeeDAO.findAll()).thenReturn(employees);

        List<Employee> result = resource.listEmployee();

        assertEquals(2, result.size());
        assertEquals("John", result.get(0).getFirstName());
        assertEquals("Jane", result.get(1).getFirstName());
        verify(employeeDAO).findAll();
    }

    @Test
    public void testListEmployeesEmpty() {
        when(employeeDAO.findAll()).thenReturn(Collections.emptyList());

        List<Employee> result = resource.listEmployee();

        assertNotNull(result);
        assertEquals(0, result.size());
        verify(employeeDAO).findAll();
    }
}
