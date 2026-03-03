package com.dropwizard.employee.db;

import com.dropwizard.employee.core.Employee;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Optional;

/**
 * Data Access Object for Employee entity.
 * Uses Dropwizard's Hibernate integration with Java 8 Optional for null-safe lookups.
 *
 * @author mchougule
 * @since 1.0.0
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
        return list(namedQuery("com.dropwizard.employee.core.Employee.findAll"));
    }
}
