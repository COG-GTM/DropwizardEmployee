package com.dropwizard.employee;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * Unit test for the App class.
 * Modernized from JUnit 3 (TestCase) to JUnit 4 annotations.
 */
public class AppTest {

    @Test
    public void testAppInstantiation() {
        App app = new App();
        assertNotNull("App instance should not be null", app);
    }
}
