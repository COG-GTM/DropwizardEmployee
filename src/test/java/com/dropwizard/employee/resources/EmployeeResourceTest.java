package com.dropwizard.employee.resources;

import com.dropwizard.employee.core.Employee;
import com.dropwizard.employee.db.EmployeeDAO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.hibernate.UnitOfWorkApplicationListener;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.context.internal.ManagedSessionContext;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.concurrent.Callable;

import static org.assertj.core.api.Assertions.assertThat;

public class EmployeeResourceTest {

    private static final SessionFactory SESSION_FACTORY = new Configuration()
            .setProperty("hibernate.connection.driver_class", "org.h2.Driver")
            .setProperty("hibernate.connection.url", "jdbc:h2:mem:employee-resource-test;DB_CLOSE_DELAY=-1")
            .setProperty("hibernate.connection.username", "sa")
            .setProperty("hibernate.connection.password", "")
            .setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect")
            .setProperty("hibernate.hbm2ddl.auto", "create-drop")
            .setProperty("hibernate.current_session_context_class", "managed")
            .addAnnotatedClass(Employee.class)
            .buildSessionFactory();

    private static final EmployeeDAO DAO = new EmployeeDAO(SESSION_FACTORY);

    @ClassRule
    public static final ResourceTestRule RESOURCES = ResourceTestRule.builder()
            .addResource(new EmployeeResource(DAO))
            .addProvider(new UnitOfWorkApplicationListener("hibernate", SESSION_FACTORY))
            .build();

    private final ObjectMapper mapper = Jackson.newObjectMapper();

    private Employee victim;

    @AfterClass
    public static void closeSessionFactory() {
        SESSION_FACTORY.close();
    }

    @Before
    public void seedVictim() throws Exception {
        victim = inTransaction(new Callable<Employee>() {
            @Override
            public Employee call() {
                return DAO.create(new Employee("Alice", "Smith", "Engineer"));
            }
        });
    }

    @After
    public void clearTable() throws Exception {
        inTransaction(new Callable<Integer>() {
            @Override
            public Integer call() {
                return SESSION_FACTORY.getCurrentSession().createQuery("delete from Employee").executeUpdate();
            }
        });
    }

    @Test
    public void postWithExistingIdCreatesNewEmployeeAndLeavesExistingUnchanged() throws Exception {
        String body = "{\"id\": " + victim.getId()
                + ", \"firstName\": \"Mallory\", \"lastName\": \"Evil\", \"jobTitle\": \"CEO\"}";

        JsonNode created = RESOURCES.client().target("/employee").request()
                .post(Entity.entity(body, MediaType.APPLICATION_JSON_TYPE), JsonNode.class);

        assertThat(created.get("id").asLong()).isNotEqualTo(victim.getId()).isPositive();
        assertThat(created.get("firstName").asText()).isEqualTo("Mallory");

        assertThat(findAll()).hasSize(2);
        Employee stored = findVictim();
        assertThat(stored.getFirstName()).isEqualTo("Alice");
        assertThat(stored.getLastName()).isEqualTo("Smith");
        assertThat(stored.getJobTitle()).isEqualTo("Engineer");
    }

    @Test
    public void daoCreateNeverUpdatesAnExistingRow() throws Exception {
        final Employee forged = new Employee("Mallory", "Evil", "CEO");
        forged.setId(victim.getId());

        Employee created = inTransaction(new Callable<Employee>() {
            @Override
            public Employee call() {
                return DAO.create(forged);
            }
        });

        assertThat(created.getId()).isNotEqualTo(victim.getId());
        assertThat(findVictim().getFirstName()).isEqualTo("Alice");
        assertThat(findAll()).hasSize(2);
    }

    @Test
    public void jsonIgnoresClientSuppliedIdButStillExposesIt() throws Exception {
        Employee parsed = mapper.readValue(
                "{\"id\": 7, \"firstName\": \"A\", \"lastName\": \"B\", \"jobTitle\": \"C\"}", Employee.class);
        assertThat(parsed.getId()).isZero();

        assertThat(mapper.readTree(mapper.writeValueAsString(victim)).get("id").asLong())
                .isEqualTo(victim.getId());
    }

    private List<Employee> findAll() throws Exception {
        return inTransaction(new Callable<List<Employee>>() {
            @Override
            public List<Employee> call() {
                return DAO.findAll();
            }
        });
    }

    private Employee findVictim() throws Exception {
        return inTransaction(new Callable<Employee>() {
            @Override
            public Employee call() {
                return DAO.findById(victim.getId()).get();
            }
        });
    }

    private static <T> T inTransaction(Callable<T> work) throws Exception {
        Session session = SESSION_FACTORY.openSession();
        ManagedSessionContext.bind(session);
        Transaction tx = session.beginTransaction();
        try {
            T result = work.call();
            tx.commit();
            return result;
        } catch (Exception e) {
            tx.rollback();
            throw e;
        } finally {
            ManagedSessionContext.unbind(SESSION_FACTORY);
            session.close();
        }
    }
}
