package com.dropwizard.employee.db;

import com.dropwizard.employee.core.Employee;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.jdbc.ReturningWork;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

/**
 * Created by mchougule on 1/16/2017.
 */
public class EmployeeDAO extends AbstractDAO<Employee> {
    private final SessionFactory sessionFactory;

    public EmployeeDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
        this.sessionFactory = sessionFactory;
    }
    public Optional<Employee> findById(Long id) {
        return Optional.ofNullable(get(id));
    }
    public Employee create(Employee person)  {
        return persist(person);
    }

    // Read-only listing: a stateless session on the unit of work's connection skips
    // registering every row in the persistence context, which dominated this call.
    public List<Employee> findAll() {
        return currentSession().doReturningWork(new ReturningWork<List<Employee>>() {
            @Override
            @SuppressWarnings("unchecked")
            public List<Employee> execute(Connection connection) {
                StatelessSession session = sessionFactory.openStatelessSession(connection);
                try {
                    return session.getNamedQuery("com.dropwizard.employee.core.Employee.findAll").list();
                } finally {
                    session.close();
                }
            }
        });
    }
}
