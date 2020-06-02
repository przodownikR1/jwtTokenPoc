package pl.scalatech.auth.jwtsecurity;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import pl.scalatech.auth.jwtsecurity.infrastructure.security.JwtSetting;

@SpringBootApplication
@Slf4j
@EnableConfigurationProperties(JwtSetting.class)
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }


}
