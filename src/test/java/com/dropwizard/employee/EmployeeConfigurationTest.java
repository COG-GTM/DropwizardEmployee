package com.dropwizard.employee;

import com.dropwizard.employee.core.Template;
import io.dropwizard.db.DataSourceFactory;
import org.junit.Test;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class EmployeeConfigurationTest {

    @Test
    public void templateGetterAndSetter() {
        EmployeeConfiguration config = new EmployeeConfiguration();
        config.setTemplate("Hello, %s!");

        assertThat(config.getTemplate()).isEqualTo("Hello, %s!");
    }

    @Test
    public void defaultNameHasDefaultValue() {
        EmployeeConfiguration config = new EmployeeConfiguration();
        assertThat(config.getDefaultName()).isEqualTo("Employee!");
    }

    @Test
    public void defaultNameGetterAndSetter() {
        EmployeeConfiguration config = new EmployeeConfiguration();
        config.setDefaultName("World");

        assertThat(config.getDefaultName()).isEqualTo("World");
    }

    @Test
    public void dataSourceFactoryGetterAndSetter() {
        EmployeeConfiguration config = new EmployeeConfiguration();
        DataSourceFactory dsf = new DataSourceFactory();
        config.setDataSourceFactory(dsf);

        assertThat(config.getDataSourceFactory()).isSameAs(dsf);
    }

    @Test
    public void dataSourceFactoryDefaultIsNotNull() {
        EmployeeConfiguration config = new EmployeeConfiguration();
        assertThat(config.getDataSourceFactory()).isNotNull();
    }

    @Test
    public void viewRendererConfigurationDefaultIsEmpty() {
        EmployeeConfiguration config = new EmployeeConfiguration();
        assertThat(config.getViewRendererConfiguration()).isEmpty();
    }

    @Test
    public void buildTemplateCreatesTemplateFromConfig() {
        EmployeeConfiguration config = new EmployeeConfiguration();
        config.setTemplate("Hi %s!");
        config.setDefaultName("Default");

        Template template = config.buildTemplate();

        assertThat(template.render(Optional.<String>of("Test"))).isEqualTo("Hi Test!");
        assertThat(template.render(Optional.<String>empty())).isEqualTo("Hi Default!");
    }
}
