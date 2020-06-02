package pl.scalatech.auth.jwtsecurity.port;

import pl.scalatech.auth.jwtsecurity.infrastructure.security.UserAuth;

import java.util.Optional;
import java.util.Set;

public interface UserRepositoryPort {
    Optional<UserAuth> findByUsername(String username);

    void save(UserAuth userThree);

    Set<UserAuth> findAll();
}
