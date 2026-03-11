package com.dropwizard.employee.core;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@link Employee} entity.
 * Demonstrates Java 8 features: lambdas, streams, Optional.
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
        assertNotNull("Default constructor should create a non-null instance", emp);
    }

    @Test
    public void testParameterizedConstructor() {
        assertEquals("John", employee.getFirstName());
        assertEquals("Doe", employee.getLastName());
        assertEquals("Software Engineer", employee.getJobTitle());
    }

    @Test
    public void testSettersAndGetters() {
        employee.setId(1L);
        employee.setFirstName("Jane");
        employee.setLastName("Smith");
        employee.setJobTitle("Manager");

        assertEquals(1L, employee.getId());
        assertEquals("Jane", employee.getFirstName());
        assertEquals("Smith", employee.getLastName());
        assertEquals("Manager", employee.getJobTitle());
    }

    @Test
    public void testEqualsWithSameObject() {
        assertTrue("Employee should be equal to itself", employee.equals(employee));
    }

    @Test
    public void testEqualsWithEqualObjects() {
        Employee other = new Employee("John", "Doe", "Software Engineer");
        assertTrue("Employees with same fields should be equal", employee.equals(other));
    }

    @Test
    public void testEqualsWithDifferentObjects() {
        Employee other = new Employee("Jane", "Doe", "Manager");
        assertFalse("Employees with different fields should not be equal", employee.equals(other));
    }

    @Test
    public void testEqualsWithNull() {
        assertFalse("Employee should not be equal to null", employee.equals(null));
    }

    @Test
    public void testEqualsWithDifferentType() {
        assertFalse("Employee should not be equal to a non-Employee object", employee.equals("not an employee"));
    }

    @Test
    public void testHashCodeConsistency() {
        Employee other = new Employee("John", "Doe", "Software Engineer");
        assertEquals("Equal employees should have the same hash code",
                employee.hashCode(), other.hashCode());
    }

    @Test
    public void testHashCodeDifference() {
        Employee other = new Employee("Jane", "Smith", "Manager");
        assertNotEquals("Different employees should likely have different hash codes",
                employee.hashCode(), other.hashCode());
    }

    @Test
    public void testToStringContainsFields() {
        String str = employee.toString();
        assertNotNull("toString should not return null", str);
        assertTrue("toString should contain class name", str.contains("Employee"));
        assertTrue("toString should contain firstName", str.contains("John"));
        assertTrue("toString should contain lastName", str.contains("Doe"));
        assertTrue("toString should contain jobTitle", str.contains("Software Engineer"));
    }

    /**
     * Test using Java 8 streams to filter a list of employees by job title.
     */
    @Test
    public void testStreamFilterByJobTitle() {
        List<Employee> employees = Arrays.asList(
                new Employee("John", "Doe", "Software Engineer"),
                new Employee("Jane", "Smith", "Manager"),
                new Employee("Bob", "Brown", "Software Engineer"),
                new Employee("Alice", "White", "Designer")
        );

        List<Employee> engineers = employees.stream()
                .filter(e -> "Software Engineer".equals(e.getJobTitle()))
                .collect(Collectors.toList());

        assertEquals("Should find 2 Software Engineers", 2, engineers.size());
        assertTrue("All filtered employees should be Software Engineers",
                engineers.stream().allMatch(e -> "Software Engineer".equals(e.getJobTitle())));
    }

    /**
     * Test using Java 8 streams to map employee names.
     */
    @Test
    public void testStreamMapToFullNames() {
        List<Employee> employees = Arrays.asList(
                new Employee("John", "Doe", "Engineer"),
                new Employee("Jane", "Smith", "Manager")
        );

        List<String> fullNames = employees.stream()
                .map(e -> e.getFirstName() + " " + e.getLastName())
                .collect(Collectors.toList());

        assertEquals(2, fullNames.size());
        assertEquals("John Doe", fullNames.get(0));
        assertEquals("Jane Smith", fullNames.get(1));
    }

    /**
     * Test using Java 8 Optional with employee lookup simulation.
     */
    @Test
    public void testOptionalPresent() {
        Optional<Employee> optionalEmployee = Optional.of(employee);
        assertTrue("Optional should be present", optionalEmployee.isPresent());
        assertEquals("John", optionalEmployee.get().getFirstName());
    }

    @Test
    public void testOptionalEmpty() {
        Optional<Employee> optionalEmployee = Optional.empty();
        assertFalse("Optional should be empty", optionalEmployee.isPresent());
    }

    @Test
    public void testOptionalOrElse() {
        Employee defaultEmployee = new Employee("Default", "User", "Unknown");
        Optional<Employee> optionalEmployee = Optional.empty();

        Employee result = optionalEmployee.orElse(defaultEmployee);
        assertEquals("Default", result.getFirstName());
        assertEquals("Unknown", result.getJobTitle());
    }

    /**
     * Test using Java 8 stream count operation.
     */
    @Test
    public void testStreamCount() {
        List<Employee> employees = Arrays.asList(
                new Employee("John", "Doe", "Engineer"),
                new Employee("Jane", "Smith", "Manager"),
                new Employee("Bob", "Brown", "Engineer")
        );

        long engineerCount = employees.stream()
                .filter(e -> "Engineer".equals(e.getJobTitle()))
                .count();

        assertEquals("Should count 2 engineers", 2L, engineerCount);
    }
}
