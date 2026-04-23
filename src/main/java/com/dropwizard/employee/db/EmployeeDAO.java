package com.dropwizard.employee.db;

import com.dropwizard.employee.core.Employee;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Optional;

/**
 * Created by mchougule on 1/16/2017.
 */
public class EmployeeDAO extends AbstractDAO<Employee> {
    public EmployeeDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }
    public Optional<Employee> findById(Long id) {
        return Optional.ofNullable(get(id));
    }
    public Employee create(Employee person)  {
        return persist(person);
    }

    public List<Employee> findAll() {
        return currentSession().createNamedQuery("com.dropwizard.employee.core.Employee.findAll", Employee.class).list();
    }
}
