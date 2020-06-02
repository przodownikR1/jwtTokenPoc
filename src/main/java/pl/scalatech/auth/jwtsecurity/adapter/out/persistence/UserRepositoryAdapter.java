package pl.scalatech.auth.jwtsecurity.adapter.out.persistence;

import pl.scalatech.auth.jwtsecurity.infrastructure.security.UserAuth;
import pl.scalatech.auth.jwtsecurity.port.UserRepositoryPort;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

class UserRepositoryAdapter implements UserRepositoryPort {
    private final ConcurrentMap<String, UserAuth> store = new ConcurrentHashMap<>();

    @Override
    public Optional<UserAuth> findByUsername(String username) {
        return Optional.of(store.get(username));

    }

    @Override
    public void save(UserAuth user) {
        store.put(user.getEmail(), user);
    }

    @Override
    public Set<UserAuth> findAll() {
        return new HashSet<>(store.values());
    }
}
