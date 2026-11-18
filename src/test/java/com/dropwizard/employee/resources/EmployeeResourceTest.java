package com.dropwizard.employee.resources;

import com.dropwizard.employee.core.Employee;
import com.dropwizard.employee.db.EmployeeDAO;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class EmployeeResourceTest {
    private final EmployeeDAO dao = mock(EmployeeDAO.class);
    private final EmployeeResource resource = new EmployeeResource(dao);

    @Test
    public void createDelegatesToDaoAndReturnsPersistedEmployee() {
        Employee input = new Employee("Mayur", "Chougule", "Software Engineer");
        Employee saved = new Employee("Mayur", "Chougule", "Software Engineer");
        saved.setId(1L);
        when(dao.create(input)).thenReturn(saved);

        assertSame(saved, resource.createEmployee(input));
        verify(dao).create(input);
    }

    @Test
    public void listReturnsAllEmployeesFromDao() {
        List<Employee> all = Arrays.asList(
                new Employee("Mayur", "Chougule", "Software Engineer"),
                new Employee("Ada", "Lovelace", "Analyst"));
        when(dao.findAll()).thenReturn(all);

        assertEquals(all, resource.listEmployee());
    }
}
