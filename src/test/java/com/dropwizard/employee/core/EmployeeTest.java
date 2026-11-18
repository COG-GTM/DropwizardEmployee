package com.dropwizard.employee.core;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.dropwizard.employee.EmployeeApplication;
import com.dropwizard.employee.EmployeeConfiguration;
import io.dropwizard.core.setup.Bootstrap;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class EmployeeTest {
    private static final ObjectMapper MAPPER = applicationObjectMapper();

    private static ObjectMapper applicationObjectMapper() {
        EmployeeApplication application = new EmployeeApplication();
        Bootstrap<EmployeeConfiguration> bootstrap = new Bootstrap<>(application);
        application.initialize(bootstrap);
        return bootstrap.getObjectMapper();
    }

    @Test
    public void serializesAllFieldsByName() throws Exception {
        Employee employee = new Employee("Mayur", "Chougule", "Software Engineer");
        employee.setId(7L);

        JsonNode json = MAPPER.readTree(MAPPER.writeValueAsString(employee));

        assertEquals(4, json.size());
        assertEquals(7L, json.get("id").asLong());
        assertEquals("Mayur", json.get("firstName").asText());
        assertEquals("Chougule", json.get("lastName").asText());
        assertEquals("Software Engineer", json.get("jobTitle").asText());
    }

    @Test
    public void deserializesWithoutIdAndMissingJobTitle() throws Exception {
        Employee employee = MAPPER.readValue("{\"firstName\":\"No\",\"lastName\":\"Title\"}", Employee.class);

        assertEquals(0L, employee.getId());
        assertEquals("No", employee.getFirstName());
        assertEquals("Title", employee.getLastName());
        assertEquals(null, employee.getJobTitle());
    }

    @Test(expected = com.fasterxml.jackson.databind.JsonMappingException.class)
    public void rejectsUnknownFields() throws Exception {
        MAPPER.readValue("{\"firstName\":\"G\",\"lastName\":\"H\",\"rank\":\"RADM\"}", Employee.class);
    }

    @Test
    public void equalityCoversAllFields() {
        Employee a = new Employee("Ada", "Lovelace", "Analyst");
        Employee b = new Employee("Ada", "Lovelace", "Analyst");

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertTrue(a.equals(a));

        b.setJobTitle("Engineer");
        assertNotEquals(a, b);
        b.setJobTitle("Analyst");
        b.setId(1L);
        assertNotEquals(a, b);
    }
}
