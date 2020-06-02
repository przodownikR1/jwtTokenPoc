package pl.scalatech.auth.jwtsecurity.infrastructure.security;

import lombok.Value;

import java.util.Set;

@Value
public class UserAuth {
    private String email;
    private String login;
    private String firstName;
    private String lastName;
    private String password;
    private Set<String> roles;
}
