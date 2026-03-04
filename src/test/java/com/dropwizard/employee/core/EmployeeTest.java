package com.dropwizard.employee.core;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the Employee entity.
 */
public class EmployeeTest {

    private Employee employee;

    @Before
    public void setUp() {
        employee = new Employee("John", "Doe", "Software Engineer");
        employee.setId(1L);
    }

    @Test
    public void testGettersAndSetters() {
        assertEquals(1L, employee.getId());
        assertEquals("John", employee.getFirstName());
        assertEquals("Doe", employee.getLastName());
        assertEquals("Software Engineer", employee.getJobTitle());

        employee.setId(2L);
        employee.setFirstName("Jane");
        employee.setLastName("Smith");
        employee.setJobTitle("Manager");

        assertEquals(2L, employee.getId());
        assertEquals("Jane", employee.getFirstName());
        assertEquals("Smith", employee.getLastName());
        assertEquals("Manager", employee.getJobTitle());
    }

    @Test
    public void testDefaultConstructor() {
        Employee emptyEmployee = new Employee();
        assertEquals(0L, emptyEmployee.getId());
    }

    @Test
    public void testParameterizedConstructor() {
        Employee emp = new Employee("Alice", "Wonder", "Designer");
        assertEquals("Alice", emp.getFirstName());
        assertEquals("Wonder", emp.getLastName());
        assertEquals("Designer", emp.getJobTitle());
    }

    @Test
    public void testEqualsWithSameObject() {
        assertTrue(employee.equals(employee));
    }

    @Test
    public void testEqualsWithEqualObject() {
        Employee other = new Employee("John", "Doe", "Software Engineer");
        other.setId(1L);
        assertTrue(employee.equals(other));
    }

    @Test
    public void testEqualsWithDifferentObject() {
        Employee other = new Employee("Jane", "Doe", "Manager");
        other.setId(2L);
        assertFalse(employee.equals(other));
    }

    @Test
    public void testEqualsWithNull() {
        assertFalse(employee.equals(null));
    }

    @Test
    public void testEqualsWithDifferentType() {
        assertFalse(employee.equals("not an employee"));
    }

    @Test
    public void testHashCodeConsistency() {
        Employee other = new Employee("John", "Doe", "Software Engineer");
        other.setId(1L);
        assertEquals(employee.hashCode(), other.hashCode());
    }

    @Test
    public void testHashCodeDifference() {
        Employee other = new Employee("Jane", "Smith", "Manager");
        other.setId(2L);
        assertNotEquals(employee.hashCode(), other.hashCode());
    }

    @Test
    public void testToString() {
        String result = employee.toString();
        assertTrue(result.contains("Employee["));
        assertTrue(result.contains("id=1"));
        assertTrue(result.contains("firstName='John'"));
        assertTrue(result.contains("lastName='Doe'"));
        assertTrue(result.contains("jobTitle='Software Engineer'"));
    }
}
