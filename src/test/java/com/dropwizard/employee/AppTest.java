package com.dropwizard.employee;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * Unit test for simple App.
 */
public class AppTest {

    @Test
    public void testAppInstantiation() {
        App app = new App();
        assertNotNull(app);
    }
}
