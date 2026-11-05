package com.dropwizard.employee.auth;

import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Authenticates HTTP Basic credentials against the users declared in the {@code auth} configuration block.
 * Startup fails if no user is configured or a password is missing/too short, so the API never runs open.
 */
public class ConfiguredUserAuthenticator implements Authenticator<BasicCredentials, User> {
    static final int MIN_PASSWORD_LENGTH = 12;

    private static final byte[] NO_USER_DIGEST = sha256("");

    private final Map<String, StoredUser> users = new HashMap<String, StoredUser>();

    public ConfiguredUserAuthenticator(List<UserCredentials> configuredUsers) {
        if (configuredUsers == null || configuredUsers.isEmpty()) {
            throw new IllegalStateException("auth.users must declare at least one API user");
        }
        for (UserCredentials credentials : configuredUsers) {
            final String username = credentials.getUsername();
            final String password = credentials.getPassword();
            if (username == null || username.trim().isEmpty()) {
                throw new IllegalStateException("auth.users entries must have a username");
            }
            if (password == null || password.length() < MIN_PASSWORD_LENGTH || password.startsWith("${")) {
                throw new IllegalStateException("auth.users password for '" + username
                        + "' must be set (e.g. via EMPLOYEE_API_PASSWORD) and be at least "
                        + MIN_PASSWORD_LENGTH + " characters");
            }
            if (credentials.getRoles() == null || credentials.getRoles().isEmpty()) {
                throw new IllegalStateException("auth.users entry '" + username + "' must have at least one role");
            }
            if (users.put(username, new StoredUser(sha256(password), new User(username, credentials.getRoles()))) != null) {
                throw new IllegalStateException("auth.users contains duplicate username '" + username + "'");
            }
        }
    }

    @Override
    public Optional<User> authenticate(BasicCredentials credentials) {
        final StoredUser stored = users.get(credentials.getUsername());
        final byte[] presented = sha256(credentials.getPassword());
        final boolean matches = MessageDigest.isEqual(stored == null ? NO_USER_DIGEST : stored.passwordDigest, presented);
        if (stored == null || !matches) {
            return Optional.empty();
        }
        return Optional.of(stored.user);
    }

    private static byte[] sha256(String value) {
        try {
            return MessageDigest.getInstance("SHA-256").digest(value.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }

    private static final class StoredUser {
        private final byte[] passwordDigest;
        private final User user;

        private StoredUser(byte[] passwordDigest, User user) {
            this.passwordDigest = passwordDigest;
            this.user = user;
        }
    }
}
