package com.dropwizard.employee.auth;

import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.auth.basic.BasicCredentials;

public final class EmployeeAuth {
    private EmployeeAuth() {
    }

    public static AuthDynamicFeature basicAuthFeature(String realm, Authenticator<BasicCredentials, User> authenticator) {
        return new AuthDynamicFeature(new BasicCredentialAuthFilter.Builder<User>()
                .setAuthenticator(authenticator)
                .setAuthorizer(new RoleAuthorizer())
                .setRealm(realm)
                .buildAuthFilter());
    }
}
