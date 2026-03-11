package com.dropwizard.employee.resources;

import com.dropwizard.employee.core.Employee;
import com.dropwizard.employee.db.EmployeeDAO;
import io.dropwizard.hibernate.UnitOfWork;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST resource for managing employees.
 * Provides endpoints for creating, listing, retrieving, and searching employees.
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
    @Consumes(MediaType.APPLICATION_JSON)
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
    public Optional<Employee> getEmployee(@PathParam("id") Long id) {
        return employeeDAO.findById(id);
    }

    /**
     * Search employees by job title using Java 8 streams.
     * Example: GET /employee/search?jobTitle=Engineer
     */
    @GET
    @Path("/search")
    @UnitOfWork
    public List<Employee> searchByJobTitle(@QueryParam("jobTitle") String jobTitle) {
        if (jobTitle == null || jobTitle.isEmpty()) {
            return employeeDAO.findAll();
        }
        return employeeDAO.findAll().stream()
                .filter(e -> e.getJobTitle() != null
                        && e.getJobTitle().toLowerCase().contains(jobTitle.toLowerCase()))
                .collect(Collectors.toList());
    }
}
