package com.dropwizard.employee.auth;

import java.security.Principal;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class User implements Principal {
    public static final String ROLE_READER = "READER";
    public static final String ROLE_WRITER = "WRITER";

    private final String name;
    private final Set<String> roles;

    public User(String name, Set<String> roles) {
        this.name = name;
        this.roles = Collections.unmodifiableSet(new HashSet<String>(roles));
    }

    @Override
    public String getName() {
        return name;
    }

    public Set<String> getRoles() {
        return roles;
    }
}
