package com.dropwizard.employee;

import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class EmployeeConfigurationTest {
    @Test
    public void defaultsAndTemplate() {
        EmployeeConfiguration config = new EmployeeConfiguration();
        config.setTemplate("Hello, %s!");

        assertEquals("Employee!", config.getDefaultName());
        assertNotNull(config.getDataSourceFactory());
        assertTrue(config.getViewRendererConfiguration().isEmpty());
        assertEquals("Hello, Employee!!", config.buildTemplate().render(Optional.<String>empty()));
    }
}
