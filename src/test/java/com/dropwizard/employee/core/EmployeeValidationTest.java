package com.dropwizard.employee.core;

import org.junit.BeforeClass;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class EmployeeValidationTest {

    private static Validator validator;

    @BeforeClass
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void validEmployeeHasNoViolations() {
        Employee employee = new Employee("John", "Doe", "Engineer");
        Set<ConstraintViolation<Employee>> violations = validator.validate(employee);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void nullFirstNameIsRejected() {
        Employee employee = new Employee(null, "Doe", "Engineer");
        Set<ConstraintViolation<Employee>> violations = validator.validate(employee);
        assertEquals(1, violations.size());
    }

    @Test
    public void emptyFirstNameIsRejected() {
        Employee employee = new Employee("", "Doe", "Engineer");
        Set<ConstraintViolation<Employee>> violations = validator.validate(employee);
        assertTrue(violations.size() >= 1);
    }

    @Test
    public void blankFirstNameIsRejected() {
        Employee employee = new Employee("   ", "Doe", "Engineer");
        Set<ConstraintViolation<Employee>> violations = validator.validate(employee);
        assertEquals(1, violations.size());
    }

    @Test
    public void nullLastNameIsRejected() {
        Employee employee = new Employee("John", null, "Engineer");
        Set<ConstraintViolation<Employee>> violations = validator.validate(employee);
        assertEquals(1, violations.size());
    }

    @Test
    public void emptyLastNameIsRejected() {
        Employee employee = new Employee("John", "", "Engineer");
        Set<ConstraintViolation<Employee>> violations = validator.validate(employee);
        assertTrue(violations.size() >= 1);
    }

    @Test
    public void nullJobTitleIsRejected() {
        Employee employee = new Employee("John", "Doe", null);
        Set<ConstraintViolation<Employee>> violations = validator.validate(employee);
        assertEquals(1, violations.size());
    }

    @Test
    public void emptyJobTitleIsRejected() {
        Employee employee = new Employee("John", "Doe", "");
        Set<ConstraintViolation<Employee>> violations = validator.validate(employee);
        assertTrue(violations.size() >= 1);
    }

    @Test
    public void allFieldsNullProducesThreeViolations() {
        Employee employee = new Employee(null, null, null);
        Set<ConstraintViolation<Employee>> violations = validator.validate(employee);
        assertEquals(3, violations.size());
    }

    @Test
    public void maxLengthFieldIsAccepted() {
        String name = new String(new char[255]).replace('\0', 'a');
        Employee employee = new Employee(name, name, name);
        Set<ConstraintViolation<Employee>> violations = validator.validate(employee);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void overMaxLengthFieldIsRejected() {
        String name = new String(new char[256]).replace('\0', 'a');
        Employee employee = new Employee(name, "Doe", "Engineer");
        Set<ConstraintViolation<Employee>> violations = validator.validate(employee);
        assertEquals(1, violations.size());
    }
}
