package com.dropwizard.employee.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import org.junit.Test;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.assertj.core.api.Assertions.assertThat;

public class EmployeeTest {

    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

    @Test
    public void defaultConstructorCreatesInstance() {
        Employee employee = new Employee();
        assertThat(employee).isNotNull();
    }

    @Test
    public void parameterizedConstructorSetsFields() {
        Employee employee = new Employee("John", "Doe", "Engineer");

        assertThat(employee.getFirstName()).isEqualTo("John");
        assertThat(employee.getLastName()).isEqualTo("Doe");
        assertThat(employee.getJobTitle()).isEqualTo("Engineer");
    }

    @Test
    public void settersAndGettersWork() {
        Employee employee = new Employee();

        employee.setId(1L);
        employee.setFirstName("Jane");
        employee.setLastName("Smith");
        employee.setJobTitle("Manager");

        assertThat(employee.getId()).isEqualTo(1L);
        assertThat(employee.getFirstName()).isEqualTo("Jane");
        assertThat(employee.getLastName()).isEqualTo("Smith");
        assertThat(employee.getJobTitle()).isEqualTo("Manager");
    }

    @Test
    public void equalsReturnsTrueForIdenticalEmployees() {
        Employee emp1 = new Employee("John", "Doe", "Engineer");
        emp1.setId(1L);
        Employee emp2 = new Employee("John", "Doe", "Engineer");
        emp2.setId(1L);

        assertThat(emp1).isEqualTo(emp2);
    }

    @Test
    public void equalsReturnsFalseForDifferentFirstName() {
        Employee emp1 = new Employee("John", "Doe", "Engineer");
        emp1.setId(1L);
        Employee emp2 = new Employee("Jane", "Doe", "Engineer");
        emp2.setId(1L);

        assertThat(emp1).isNotEqualTo(emp2);
    }

    @Test
    public void equalsReturnsFalseForDifferentId() {
        Employee emp1 = new Employee("John", "Doe", "Engineer");
        emp1.setId(1L);
        Employee emp2 = new Employee("John", "Doe", "Engineer");
        emp2.setId(2L);

        assertThat(emp1).isNotEqualTo(emp2);
    }

    @Test
    public void equalsReturnsTrueForSameInstance() {
        Employee emp = new Employee("John", "Doe", "Engineer");
        assertThat(emp).isEqualTo(emp);
    }

    @Test
    public void equalsReturnsFalseForNull() {
        Employee emp = new Employee("John", "Doe", "Engineer");
        assertThat(emp).isNotEqualTo(null);
    }

    @Test
    public void equalsReturnsFalseForDifferentType() {
        Employee emp = new Employee("John", "Doe", "Engineer");
        assertThat(emp).isNotEqualTo("not an employee");
    }

    @Test
    public void hashCodeConsistentForEqualObjects() {
        Employee emp1 = new Employee("John", "Doe", "Engineer");
        emp1.setId(1L);
        Employee emp2 = new Employee("John", "Doe", "Engineer");
        emp2.setId(1L);

        assertThat(emp1.hashCode()).isEqualTo(emp2.hashCode());
    }

    @Test
    public void hashCodeDiffersForDifferentObjects() {
        Employee emp1 = new Employee("John", "Doe", "Engineer");
        emp1.setId(1L);
        Employee emp2 = new Employee("Jane", "Smith", "Manager");
        emp2.setId(2L);

        assertThat(emp1.hashCode()).isNotEqualTo(emp2.hashCode());
    }

    @Test
    public void serializesToJson() throws Exception {
        Employee employee = new Employee("John", "Doe", "Engineer");
        employee.setId(1L);

        String expected = MAPPER.writeValueAsString(
                MAPPER.readValue(fixture("fixtures/employee.json"), Employee.class));

        assertThat(MAPPER.writeValueAsString(employee)).isEqualTo(expected);
    }

    @Test
    public void deserializesFromJson() throws Exception {
        Employee employee = new Employee("John", "Doe", "Engineer");
        employee.setId(1L);

        Employee deserialized = MAPPER.readValue(fixture("fixtures/employee.json"), Employee.class);

        assertThat(deserialized.getFirstName()).isEqualTo(employee.getFirstName());
        assertThat(deserialized.getLastName()).isEqualTo(employee.getLastName());
        assertThat(deserialized.getJobTitle()).isEqualTo(employee.getJobTitle());
    }
}
