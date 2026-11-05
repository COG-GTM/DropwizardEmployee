package com.dropwizard.employee.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.Set;

public class UserCredentials {

    @JsonProperty
    private String username;

    @JsonProperty
    private String password;

    @JsonProperty
    private Set<String> roles = Collections.emptySet();

    public UserCredentials() {
    }

    public UserCredentials(String username, String password, Set<String> roles) {
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Set<String> getRoles() {
        return roles;
    }

    @Override
    public String toString() {
        return "UserCredentials{username='" + username + "', roles=" + roles + "}";
    }
}
