package com.dropwizard.employee.core;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the Employee entity.
 * Validates equals, hashCode, toString, and getter/setter behavior.
 */
public class EmployeeTest {

    private Employee employee;

    @Before
    public void setUp() {
        employee = new Employee("John", "Doe", "Software Engineer");
        employee.setId(1L);
    }

    @Test
    public void testGettersReturnCorrectValues() {
        assertEquals("John", employee.getFirstName());
        assertEquals("Doe", employee.getLastName());
        assertEquals("Software Engineer", employee.getJobTitle());
        assertEquals(1L, employee.getId());
    }

    @Test
    public void testSettersUpdateValues() {
        employee.setFirstName("Jane");
        employee.setLastName("Smith");
        employee.setJobTitle("Product Manager");
        employee.setId(2L);

        assertEquals("Jane", employee.getFirstName());
        assertEquals("Smith", employee.getLastName());
        assertEquals("Product Manager", employee.getJobTitle());
        assertEquals(2L, employee.getId());
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
    public void testEqualsWithDifferentId() {
        Employee other = new Employee("John", "Doe", "Software Engineer");
        other.setId(2L);
        assertFalse(employee.equals(other));
    }

    @Test
    public void testEqualsWithDifferentFirstName() {
        Employee other = new Employee("Jane", "Doe", "Software Engineer");
        other.setId(1L);
        assertFalse(employee.equals(other));
    }

    @Test
    public void testEqualsWithDifferentType() {
        assertFalse(employee.equals("not an employee"));
    }

    @Test
    public void testEqualsWithNull() {
        assertFalse(employee.equals(null));
    }

    @Test
    public void testHashCodeConsistency() {
        Employee other = new Employee("John", "Doe", "Software Engineer");
        other.setId(1L);
        assertEquals(employee.hashCode(), other.hashCode());
    }

    @Test
    public void testHashCodeDifference() {
        Employee other = new Employee("Jane", "Doe", "Software Engineer");
        other.setId(2L);
        assertNotEquals(employee.hashCode(), other.hashCode());
    }

    @Test
    public void testToString() {
        String result = employee.toString();
        assertTrue(result.contains("John"));
        assertTrue(result.contains("Doe"));
        assertTrue(result.contains("Software Engineer"));
        assertTrue(result.contains("Employee["));
    }

    @Test
    public void testToStringFormat() {
        String expected = "Employee[id=1, firstName='John', lastName='Doe', jobTitle='Software Engineer']";
        assertEquals(expected, employee.toString());
    }

    @Test
    public void testDefaultConstructor() {
        Employee empty = new Employee();
        assertEquals(0L, empty.getId());
        assertEquals(null, empty.getFirstName());
        assertEquals(null, empty.getLastName());
        assertEquals(null, empty.getJobTitle());
    }
}
