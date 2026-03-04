package com.dropwizard.employee.core;

import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the Template class.
 */
public class TemplateTest {

    @Test
    public void testRenderWithName() {
        Template template = new Template("Hello, %s!", "Stranger");
        String result = template.render(Optional.of("World"));
        assertEquals("Hello, World!", result);
    }

    @Test
    public void testRenderWithEmptyOptionalUsesDefault() {
        Template template = new Template("Hello, %s!", "Stranger");
        String result = template.render(Optional.empty());
        assertEquals("Hello, Stranger!", result);
    }

    @Test
    public void testRenderWithDifferentTemplate() {
        Template template = new Template("Welcome, %s!", "Guest");
        String result = template.render(Optional.of("Admin"));
        assertEquals("Welcome, Admin!", result);
    }
}
