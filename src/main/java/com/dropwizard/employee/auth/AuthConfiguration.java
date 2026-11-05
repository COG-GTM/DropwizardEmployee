package com.dropwizard.employee.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;

public class AuthConfiguration {

    @NotEmpty
    @JsonProperty
    private String realm = "employee-api";

    @NotNull
    @JsonProperty
    private List<UserCredentials> users = Collections.emptyList();

    public AuthConfiguration() {
    }

    public AuthConfiguration(String realm, List<UserCredentials> users) {
        this.realm = realm;
        this.users = users;
    }

    public String getRealm() {
        return realm;
    }

    public List<UserCredentials> getUsers() {
        return users;
    }
}
