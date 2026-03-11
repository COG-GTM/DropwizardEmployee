package com.dropwizard.employee.db;

import com.dropwizard.employee.core.Employee;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Optional;

/**
 * Data Access Object for {@link Employee} entities.
 * Uses Java 8 {@link Optional} for null-safe lookups.
 */
public class EmployeeDAO extends AbstractDAO<Employee> {

    public EmployeeDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    /**
     * Find an employee by ID, returning an {@link Optional} to handle missing entries.
     *
     * @param id the employee ID
     * @return an Optional containing the employee if found, or empty otherwise
     */
    public Optional<Employee> findById(Long id) {
        return Optional.ofNullable(get(id));
    }

    /**
     * Persist a new employee.
     *
     * @param employee the employee to create
     * @return the persisted employee with generated ID
     */
    public Employee create(Employee employee) {
        return persist(employee);
    }

    /**
     * Retrieve all employees.
     *
     * @return a list of all employees
     */
    public List<Employee> findAll() {
        return list(namedQuery("com.dropwizard.employee.core.Employee.findAll"));
    }
}
