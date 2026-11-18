package com.dropwizard.employee.core;

import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class TemplateTest {
    private final Template template = new Template("Hello, %s!", "Stranger");

    @Test
    public void rendersSuppliedName() {
        assertEquals("Hello, Mayur!", template.render(Optional.of("Mayur")));
    }

    @Test
    public void rendersDefaultNameWhenAbsent() {
        assertEquals("Hello, Stranger!", template.render(Optional.<String>empty()));
    }
}
