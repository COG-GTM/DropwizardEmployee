package com.dropwizard.employee.db;

import com.dropwizard.employee.core.Employee;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import org.hibernate.context.internal.ManagedSessionContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EmployeeDAOTest {
    private SessionFactory sessionFactory;
    private EmployeeDAO dao;
    private Session session;

    @Before
    public void setUp() {
        sessionFactory = new Configuration()
                .addAnnotatedClass(Employee.class)
                .setProperty("hibernate.connection.driver_class", "org.h2.Driver")
                .setProperty("hibernate.connection.url", "jdbc:h2:mem:employee-dao-test;DB_CLOSE_DELAY=-1")
                .setProperty("hibernate.connection.username", "sa")
                .setProperty("hibernate.connection.password", "")
                .setProperty("hibernate.hbm2ddl.auto", "create-drop")
                .setProperty("hibernate.current_session_context_class", "managed")
                .buildSessionFactory();
        dao = new EmployeeDAO(sessionFactory);
        session = sessionFactory.openSession();
        ManagedSessionContext.bind(session);
        session.beginTransaction();
    }

    @After
    public void tearDown() {
        if (session.getTransaction().getStatus() == TransactionStatus.ACTIVE) {
            session.getTransaction().rollback();
        }
        ManagedSessionContext.unbind(sessionFactory);
        session.close();
        sessionFactory.close();
    }

    @Test
    public void findAllMatchesTheNamedQueryOnTheCurrentSession() {
        for (int i = 0; i < 50; i++) {
            dao.create(new Employee("First" + i, "Last" + i, "Title" + (i % 3)));
        }
        session.flush();
        session.clear();

        @SuppressWarnings("unchecked")
        List<Employee> expected = session.getNamedQuery("com.dropwizard.employee.core.Employee.findAll").list();
        session.clear();

        List<Employee> actual = dao.findAll();

        assertEquals(50, actual.size());
        assertEquals(expected, actual);
        assertEquals("First0", actual.get(0).getFirstName());
        assertTrue(actual.get(0).getId() > 0);
    }

    @Test
    public void findAllSeesRowsWrittenEarlierInTheSameUnitOfWork() {
        dao.create(new Employee("Ada", "Lovelace", "Engineer"));
        session.flush();

        List<Employee> actual = dao.findAll();

        assertEquals(1, actual.size());
        assertEquals("Lovelace", actual.get(0).getLastName());
    }

    @Test
    public void findAllDoesNotAttachRowsToTheSession() {
        dao.create(new Employee("Grace", "Hopper", "Admiral"));
        session.flush();
        session.clear();

        List<Employee> actual = dao.findAll();

        assertEquals(1, actual.size());
        assertFalse(session.contains(actual.get(0)));
    }
}
