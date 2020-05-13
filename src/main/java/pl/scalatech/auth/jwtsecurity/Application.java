package pl.scalatech.auth.jwtsecurity;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import pl.scalatech.auth.jwtsecurity.infrastucture.security.JwtSetting;
import pl.scalatech.auth.jwtsecurity.infrastucture.security.UserAuth;
import pl.scalatech.auth.jwtsecurity.port.UserRepositoryPort;

import java.util.Set;

@SpringBootApplication
@Slf4j
@EnableConfigurationProperties(JwtSetting.class)
public class Application {
    @Autowired
    private UserRepositoryPort userRepositoryPort;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    CommandLineRunner init() {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
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
