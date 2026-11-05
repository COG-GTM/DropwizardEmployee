package com.dropwizard.employee.resources;

import com.dropwizard.employee.auth.ConfiguredUserAuthenticator;
import com.dropwizard.employee.auth.EmployeeAuth;
import com.dropwizard.employee.auth.User;
import com.dropwizard.employee.auth.UserCredentials;
import com.dropwizard.employee.core.Employee;
import com.dropwizard.employee.db.EmployeeDAO;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.glassfish.jersey.test.grizzly.GrizzlyWebTestContainerFactory;
import org.junit.After;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class EmployeeResourceAuthTest {
    private static final String WRITER_PASSWORD = "writer-password-123";
    private static final String READER_PASSWORD = "reader-password-123";

    private static final EmployeeDAO DAO = mock(EmployeeDAO.class);

    @ClassRule
    public static final ResourceTestRule RESOURCES = ResourceTestRule.builder()
            .setTestContainerFactory(new GrizzlyWebTestContainerFactory())
            .addProvider(EmployeeAuth.basicAuthFeature("employee-api", new ConfiguredUserAuthenticator(Arrays.asList(
                    new UserCredentials("writer", WRITER_PASSWORD,
                            new HashSet<String>(Arrays.asList(User.ROLE_READER, User.ROLE_WRITER))),
                    new UserCredentials("reader", READER_PASSWORD, Collections.singleton(User.ROLE_READER))))))
            .addProvider(RolesAllowedDynamicFeature.class)
            .addResource(new EmployeeResource(DAO))
            .build();

    private final Employee employee = new Employee("Mayur", "Chougule", "Software Engineer");

    @After
    public void tearDown() {
        reset(DAO);
    }

    @Test
    public void listWithoutCredentialsIsUnauthorized() {
        final Response response = request().get();
        assertThat(response.getStatus()).isEqualTo(401);
        assertThat(response.getHeaderString(HttpHeaders.WWW_AUTHENTICATE)).contains("Basic");
        verify(DAO, never()).findAll();
    }

    @Test
    public void createWithoutCredentialsIsUnauthorized() {
        final Response response = request().post(Entity.entity(employee, MediaType.APPLICATION_JSON_TYPE));
        assertThat(response.getStatus()).isEqualTo(401);
        verify(DAO, never()).create(any(Employee.class));
    }

    @Test
    public void wrongPasswordIsUnauthorized() {
        final Response response = request("writer", "not-the-password").get();
        assertThat(response.getStatus()).isEqualTo(401);
        verify(DAO, never()).findAll();
    }

    @Test
    public void readerCanList() {
        when(DAO.findAll()).thenReturn(Collections.singletonList(employee));
        final Response response = request("reader", READER_PASSWORD).get();
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.readEntity(String.class)).contains("Mayur");
    }

    @Test
    public void readerCannotCreate() {
        final Response response = request("reader", READER_PASSWORD)
                .post(Entity.entity(employee, MediaType.APPLICATION_JSON_TYPE));
        assertThat(response.getStatus()).isEqualTo(403);
        verify(DAO, never()).create(any(Employee.class));
    }

    @Test
    public void writerCanCreate() {
        when(DAO.create(any(Employee.class))).thenReturn(employee);
        final Response response = request("writer", WRITER_PASSWORD)
                .post(Entity.entity(employee, MediaType.APPLICATION_JSON_TYPE));
        assertThat(response.getStatus()).isEqualTo(200);
        verify(DAO).create(any(Employee.class));
    }

    private Invocation.Builder request() {
        return RESOURCES.getJerseyTest().target("/employee").request();
    }

    private Invocation.Builder request(String username, String password) {
        final String token = Base64.getEncoder()
                .encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8));
        return request().header(HttpHeaders.AUTHORIZATION, "Basic " + token);
    }
}
