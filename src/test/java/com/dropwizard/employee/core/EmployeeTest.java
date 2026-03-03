package com.dropwizard.employee.core;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@link Employee} entity class.
 * Validates constructors, getters/setters, equals, hashCode, and toString.
 */
public class EmployeeTest {

    private Employee employee;

    @Before
    public void setUp() {
        employee = new Employee("John", "Doe", "Software Engineer");
    }

    @Test
    public void testDefaultConstructor() {
        Employee emp = new Employee();
        assertEquals(0L, emp.getId());
    }

    @Test
    public void testParameterizedConstructor() {
        assertEquals("John", employee.getFirstName());
        assertEquals("Doe", employee.getLastName());
        assertEquals("Software Engineer", employee.getJobTitle());
    }

    @Test
    public void testSetAndGetId() {
        employee.setId(42L);
        assertEquals(42L, employee.getId());
    }

    @Test
    public void testSetAndGetFirstName() {
        employee.setFirstName("Jane");
        assertEquals("Jane", employee.getFirstName());
    }

    @Test
    public void testSetAndGetLastName() {
        employee.setLastName("Smith");
        assertEquals("Smith", employee.getLastName());
    }

    @Test
    public void testSetAndGetJobTitle() {
        employee.setJobTitle("Manager");
        assertEquals("Manager", employee.getJobTitle());
    }

    @Test
    public void testEqualsSameObject() {
        assertTrue(employee.equals(employee));
    }

    @Test
    public void testEqualsEqualObjects() {
        Employee other = new Employee("John", "Doe", "Software Engineer");
        assertTrue(employee.equals(other));
    }

    @Test
    public void testEqualsNotEqual() {
        Employee other = new Employee("Jane", "Doe", "Manager");
        assertFalse(employee.equals(other));
    }

    @Test
    public void testEqualsNull() {
        assertFalse(employee.equals(null));
    }

    @Test
    public void testEqualsDifferentType() {
        assertFalse(employee.equals("not an employee"));
    }

    @Test
    public void testHashCodeConsistency() {
        Employee other = new Employee("John", "Doe", "Software Engineer");
        assertEquals(employee.hashCode(), other.hashCode());
    }

    @Test
    public void testHashCodeDifference() {
        Employee other = new Employee("Jane", "Doe", "Manager");
        assertNotEquals(employee.hashCode(), other.hashCode());
    }

    @Test
    public void testToString() {
        String str = employee.toString();
        assertTrue(str.contains("John"));
        assertTrue(str.contains("Doe"));
        assertTrue(str.contains("Software Engineer"));
        assertTrue(str.startsWith("Employee["));
    }
}
