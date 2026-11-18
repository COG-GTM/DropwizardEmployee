package com.dropwizard.employee.resources;

import com.dropwizard.employee.core.Employee;
import com.dropwizard.employee.db.EmployeeDAO;
import io.dropwizard.hibernate.UnitOfWork;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
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

    // TODO: Add a view here!
//    @GET
//    @Path("/view_mustache")
//    @UnitOfWork
//    @Produces(MediaType.TEXT_HTML)
//    public EmployeeView getPersonViewMustache(@PathParam("personId") LongParam personId) {
//        return new EmployeeView(EmployeeView.Template.MUSTACHE, findSafely(personId.get()));
//    }

    @GET
    @UnitOfWork
    public List<Employee> listEmployee() {
        return employeeDAO.findAll();
    }
}
