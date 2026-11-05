package com.dropwizard.employee.auth;

import io.dropwizard.auth.basic.BasicCredentials;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class ConfiguredUserAuthenticatorTest {
    private static final String PASSWORD = "correct-horse-battery";

    private final ConfiguredUserAuthenticator authenticator = new ConfiguredUserAuthenticator(Arrays.asList(
            new UserCredentials("writer", PASSWORD, new HashSet<String>(Arrays.asList(User.ROLE_READER, User.ROLE_WRITER))),
            new UserCredentials("reader", PASSWORD + "-r", Collections.singleton(User.ROLE_READER))));

    @Test
    public void acceptsConfiguredUserWithCorrectPassword() {
        final Optional<User> user = authenticator.authenticate(new BasicCredentials("writer", PASSWORD));
        assertThat(user).isPresent();
        assertThat(user.get().getName()).isEqualTo("writer");
        assertThat(user.get().getRoles()).containsOnly(User.ROLE_READER, User.ROLE_WRITER);
    }

    @Test
    public void rejectsWrongPassword() {
        assertThat(authenticator.authenticate(new BasicCredentials("writer", PASSWORD + "x"))).isEmpty();
        assertThat(authenticator.authenticate(new BasicCredentials("writer", ""))).isEmpty();
    }

    @Test
    public void rejectsUnknownUser() {
        assertThat(authenticator.authenticate(new BasicCredentials("mallory", PASSWORD))).isEmpty();
    }

    @Test
    public void rejectsAnotherUsersPassword() {
        assertThat(authenticator.authenticate(new BasicCredentials("reader", PASSWORD))).isEmpty();
    }

    @Test
    public void refusesToStartWithoutUsers() {
        assertRejected(Collections.<UserCredentials>emptyList());
    }

    @Test
    public void refusesToStartWithMissingOrUnresolvedPassword() {
        for (String password : Arrays.<String>asList(null, "", "short", "${EMPLOYEE_API_PASSWORD}")) {
            assertRejected(Collections.singletonList(
                    new UserCredentials("admin", password, Collections.singleton(User.ROLE_WRITER))));
        }
    }

    @Test
    public void refusesToStartWithUserWithoutRoles() {
        assertRejected(Collections.singletonList(
                new UserCredentials("admin", PASSWORD, Collections.<String>emptySet())));
    }

    @Test
    public void refusesToStartWithDuplicateUsernames() {
        assertRejected(Arrays.asList(
                new UserCredentials("admin", PASSWORD, Collections.singleton(User.ROLE_WRITER)),
                new UserCredentials("admin", PASSWORD + "-2", Collections.singleton(User.ROLE_READER))));
    }

    private static void assertRejected(List<UserCredentials> users) {
        try {
            new ConfiguredUserAuthenticator(users);
            fail("expected IllegalStateException for " + users);
        } catch (IllegalStateException expected) {
            assertThat(expected.getMessage()).doesNotContain(PASSWORD);
        }
    }
}
