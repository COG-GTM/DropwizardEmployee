package com.dropwizard.employee.resources;

import com.dropwizard.employee.core.Employee;
import com.dropwizard.employee.db.EmployeeDAO;
import io.dropwizard.hibernate.UnitOfWork;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * RESTful resource for Employee CRUD operations.
 * Uses Java 8 Optional and Stream API for cleaner data handling.
 *
 * @author mchougule
 * @since 1.0.0
 */
@Path("/employee")
@Produces(MediaType.APPLICATION_JSON)
public class EmployeeResource {

    private final EmployeeDAO employeeDAO;

    public EmployeeResource(EmployeeDAO employeeDAO) {
        this.employeeDAO = employeeDAO;
    }

    @POST
    @UnitOfWork
    public Employee createEmployee(Employee employee) {
        return employeeDAO.create(employee);
    }

    @GET
    @UnitOfWork
    public List<Employee> listEmployee() {
        return employeeDAO.findAll();
    }

    @GET
    @Path("/{id}")
    @UnitOfWork
    public Response getEmployee(@PathParam("id") Long id) {
        Optional<Employee> employee = employeeDAO.findById(id);
        return employee
                .map(e -> Response.ok(e).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @GET
    @Path("/jobtitle/{jobTitle}")
    @UnitOfWork
    public List<Employee> getEmployeesByJobTitle(@PathParam("jobTitle") String jobTitle) {
        return employeeDAO.findAll().stream()
                .filter(e -> e.getJobTitle().equalsIgnoreCase(jobTitle))
                .collect(Collectors.toList());
    }
}
