package com.dropwizard.employee.core;

import org.junit.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class TemplateTest {

    @Test
    public void rendersWithProvidedName() {
        Template template = new Template("Hello, %s!", "Stranger");
        assertThat(template.render(Optional.<String>of("World"))).isEqualTo("Hello, World!");
    }

    @Test
    public void rendersWithDefaultNameWhenEmpty() {
        Template template = new Template("Hello, %s!", "Stranger");
        assertThat(template.render(Optional.<String>empty())).isEqualTo("Hello, Stranger!");
    }

    @Test
    public void rendersWithDifferentContentFormat() {
        Template template = new Template("Welcome %s to the system", "Guest");
        assertThat(template.render(Optional.<String>of("Admin"))).isEqualTo("Welcome Admin to the system");
    }

    @Test
    public void rendersDefaultWithDifferentContentFormat() {
        Template template = new Template("Welcome %s to the system", "Guest");
        assertThat(template.render(Optional.<String>empty())).isEqualTo("Welcome Guest to the system");
    }
}
