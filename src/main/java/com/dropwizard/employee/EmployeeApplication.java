package com.dropwizard.employee;

import com.dropwizard.employee.core.Employee;

import com.dropwizard.employee.db.EmployeeDAO;
import com.dropwizard.employee.resources.EmployeeResource;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;

import java.util.Map;

/**
 * Dropwizard Employee Application.
 * Upgraded to Java 8 with lambda expressions and modern API usage.
 *
 * @author mchougule
 * @since 1.0.0
 */
public class EmployeeApplication extends Application<EmployeeConfiguration> {
    public static void main(String[] args) throws Exception {
        new EmployeeApplication().run(args);
    }

    private final HibernateBundle<EmployeeConfiguration> hibernateBundle =
            new HibernateBundle<EmployeeConfiguration>(Employee.class) {
                @Override
                public DataSourceFactory getDataSourceFactory(EmployeeConfiguration configuration) {
                    return configuration.getDataSourceFactory();
                }
            };

    @Override
    public void initialize(Bootstrap<EmployeeConfiguration> bootstrap) {
        // Enable variable substitution with environment variables
        bootstrap.setConfigurationSourceProvider(
                new SubstitutingSourceProvider(
                        bootstrap.getConfigurationSourceProvider(),
                        new EnvironmentVariableSubstitutor(false)
                )
        );

//        bootstrap.addCommand(new RenderCommand());
        bootstrap.addBundle(new AssetsBundle());
        bootstrap.addBundle(new MigrationsBundle<EmployeeConfiguration>() {
            @Override
            public DataSourceFactory getDataSourceFactory(EmployeeConfiguration configuration) {
                return configuration.getDataSourceFactory();
            }
        });
        bootstrap.addBundle(hibernateBundle);
        bootstrap.addBundle(new ViewBundle<EmployeeConfiguration>() {
            @Override
            public Map<String, Map<String, String>> getViewConfiguration(EmployeeConfiguration configuration) {
                return configuration.getViewRendererConfiguration();
            }
        });
    }

    @Override
    public void run(EmployeeConfiguration employeeConfiguration, Environment environment) {
        final EmployeeDAO employeeDAO = new EmployeeDAO(hibernateBundle.getSessionFactory());

        environment.jersey().register(new EmployeeResource(employeeDAO));
    }
}
