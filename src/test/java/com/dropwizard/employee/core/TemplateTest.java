package com.dropwizard.employee.core;

import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the Template class.
 * Validates Java 8 Optional usage in the render method.
 */
public class TemplateTest {

    private Template template;

    @Before
    public void setUp() {
        template = new Template("Hello, %s!", "Stranger");
    }

    @Test
    public void testRenderWithName() {
        String result = template.render(Optional.of("World"));
        assertEquals("Hello, World!", result);
    }

    @Test
    public void testRenderWithEmptyOptional() {
        String result = template.render(Optional.empty());
        assertEquals("Hello, Stranger!", result);
    }

    @Test
    public void testRenderWithDifferentTemplate() {
        Template greetTemplate = new Template("Welcome, %s!", "Guest");
        String result = greetTemplate.render(Optional.of("Admin"));
        assertEquals("Welcome, Admin!", result);
    }

    @Test
    public void testRenderDefaultWithEmptyOptional() {
        Template greetTemplate = new Template("Welcome, %s!", "Guest");
        String result = greetTemplate.render(Optional.empty());
        assertEquals("Welcome, Guest!", result);
    }
}
