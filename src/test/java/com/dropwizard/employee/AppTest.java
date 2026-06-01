package com.dropwizard.employee;

import com.dropwizard.employee.core.Employee;
import com.dropwizard.employee.core.Template;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Unit tests for the core domain classes.
 */
public class AppTest {

    @Test
    public void templateRendersProvidedName() {
        Template template = new Template("Hello, %s!", "Employee!");
        assertEquals("Hello, Mayur!", template.render(Optional.of("Mayur")));
    }

    @Test
    public void templateFallsBackToDefaultName() {
        Template template = new Template("Hello, %s!", "Employee!");
        assertEquals("Hello, Employee!!", template.render(Optional.empty()));
    }

    @Test
    public void employeeEqualityIsValueBased() {
        Employee a = new Employee("Mayur", "Chougule", "Software Engineer");
        Employee b = new Employee("Mayur", "Chougule", "Software Engineer");
        Employee c = new Employee("Jane", "Doe", "Product Manager");

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, c);
    }
}
