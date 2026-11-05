package com.dropwizard.employee.auth;

import io.dropwizard.auth.Authorizer;

public class RoleAuthorizer implements Authorizer<User> {
    @Override
    public boolean authorize(User user, String role) {
        return user.getRoles().contains(role);
    }
}
