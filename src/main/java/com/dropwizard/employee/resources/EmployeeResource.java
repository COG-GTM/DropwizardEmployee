package com.dropwizard.employee.resources;

import com.dropwizard.employee.core.Employee;
import com.dropwizard.employee.db.EmployeeDAO;
import io.dropwizard.hibernate.UnitOfWork;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by mchougule on 1/16/2017.
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
}
