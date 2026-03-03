package com.dropwizard.employee.core;

import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Template} class.
 * Validates rendering with and without a provided name using Java 8 Optional.
 */
public class TemplateTest {

    private Template template;

    @Before
    public void setUp() {
        template = new Template("Hello, %s!", "DefaultUser");
    }

    @Test
    public void testRenderWithName() {
        String result = template.render(Optional.of("World"));
        assertEquals("Hello, World!", result);
    }

    @Test
    public void testRenderWithEmptyOptionalUsesDefault() {
        String result = template.render(Optional.empty());
        assertEquals("Hello, DefaultUser!", result);
    }
}
