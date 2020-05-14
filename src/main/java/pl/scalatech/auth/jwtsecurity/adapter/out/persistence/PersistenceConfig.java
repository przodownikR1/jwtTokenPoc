package pl.scalatech.auth.jwtsecurity.adapter.out.persistence;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.scalatech.auth.jwtsecurity.port.UserRepositoryPort;

@Configuration
class PersistenceConfig {
    @Bean
    UserRepositoryPort userRepositoryPort() {
        return new UserRepositoryAdapter();
    }

}
