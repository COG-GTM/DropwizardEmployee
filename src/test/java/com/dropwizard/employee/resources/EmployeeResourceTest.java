package com.dropwizard.employee.resources;

import com.dropwizard.employee.core.Employee;
import com.dropwizard.employee.db.EmployeeDAO;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class EmployeeResourceTest {

    private EmployeeDAO dao;
    private EmployeeResource resource;

    @Before
    public void setUp() {
        dao = mock(EmployeeDAO.class);
        resource = new EmployeeResource(dao);
    }

    @Test
    public void listEmployeesReturnsEmptyList() {
        when(dao.findAll()).thenReturn(Collections.<Employee>emptyList());

        List<Employee> result = resource.listEmployee();

        assertThat(result).isEmpty();
        verify(dao).findAll();
    }

    @Test
    public void listEmployeesReturnsAllEmployees() {
        Employee emp1 = new Employee("John", "Doe", "Engineer");
        Employee emp2 = new Employee("Jane", "Smith", "Manager");
        when(dao.findAll()).thenReturn(Arrays.asList(emp1, emp2));

        List<Employee> result = resource.listEmployee();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getFirstName()).isEqualTo("John");
        assertThat(result.get(1).getFirstName()).isEqualTo("Jane");
        verify(dao).findAll();
    }

    @Test
    public void createEmployeeDelegatesToDao() {
        Employee input = new Employee("Alice", "Wonder", "Designer");
        Employee persisted = new Employee("Alice", "Wonder", "Designer");
        persisted.setId(1L);
        when(dao.create(input)).thenReturn(persisted);

        Employee result = resource.createEmployee(input);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getFirstName()).isEqualTo("Alice");
        assertThat(result.getLastName()).isEqualTo("Wonder");
        assertThat(result.getJobTitle()).isEqualTo("Designer");
        verify(dao).create(input);
    }

    @Test
    public void listEmployeesReturnsSingleEmployee() {
        Employee emp = new Employee("Bob", "Builder", "Architect");
        when(dao.findAll()).thenReturn(Collections.singletonList(emp));

        List<Employee> result = resource.listEmployee();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getLastName()).isEqualTo("Builder");
        assertThat(result.get(0).getJobTitle()).isEqualTo("Architect");
        verify(dao).findAll();
    }

    @Test
    public void createEmployeeReturnsWhatDaoReturns() {
        Employee input = new Employee("Eve", "Adams", "Tester");
        when(dao.create(input)).thenReturn(input);

        Employee result = resource.createEmployee(input);

        assertThat(result).isSameAs(input);
        verify(dao).create(input);
    }
}
