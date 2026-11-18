package com.dropwizard.employee.core;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.util.Objects;

/**
 * Created by mchougule on 1/16/2017.
 */

@Entity
@Table(name = "people")
@NamedQueries(
        {
                @NamedQuery(
                        name ="com.dropwizard.employee.core.Employee.findAll",
                        query = "SELECT e FROM Employee e"
                )
        }
)
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "firstName", nullable = false)
    private String firstName;

    @Column(name = "lastName", nullable = false)
    private String lastName;

    @Column(name = "jobTitle", nullable = false)
    private String jobTitle;

    public Employee() {
    }

    public Employee(String firstName, String lastName, String jobTitle) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.jobTitle = jobTitle;
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Employee)) {
            return false;
        }

        final Employee that = (Employee) o;

        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.firstName, that.firstName) &&
                Objects.equals(this.lastName, that.lastName) &&
                Objects.equals(this.jobTitle, that.jobTitle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, jobTitle);
    }
}
