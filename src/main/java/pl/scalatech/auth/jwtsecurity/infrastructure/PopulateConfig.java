package pl.scalatech.auth.jwtsecurity.infrastructure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import pl.scalatech.auth.jwtsecurity.infrastructure.security.JwtSetting;
import pl.scalatech.auth.jwtsecurity.infrastructure.security.UserAuth;
import pl.scalatech.auth.jwtsecurity.port.UserRepositoryPort;

import java.util.Set;

@Configuration
@Slf4j
class PopulateConfig {

    @Bean
    CommandLineRunner init(UserRepositoryPort userRepositoryPort, BCryptPasswordEncoder bCryptPasswordEncoder, JwtSetting jwtSetting) {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                log.info("JWT SETTING : {}", jwtSetting);
                UserAuth userOne = new UserAuth("przodownik@tlen.pl", "przodownik", "Slawek", "Borowiec", bCryptPasswordEncoder.encode("secret1"), Set.of("USER"));
                UserAuth userTwo = new UserAuth("kowalski@tlen.pl", "kowal", "Jan", "Kowalski", bCryptPasswordEncoder.encode("secret2"), Set.of("USER", "MANAGER"));
                UserAuth userThree = new UserAuth("admin@tlen.pl", "wespa", "Wlodzimierz", "Lenin", bCryptPasswordEncoder.encode("secret3"), Set.of("ADMIN"));
                userRepositoryPort.save(userOne);
                userRepositoryPort.save(userTwo);
                userRepositoryPort.save(userThree);
                userRepositoryPort.findAll()
                                  .stream()
                                  .forEach(u -> log.info("generated fake user : {}", u));

            }
        };
    }
}
