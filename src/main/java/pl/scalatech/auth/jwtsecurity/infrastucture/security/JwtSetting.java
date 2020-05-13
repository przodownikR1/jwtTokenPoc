package pl.scalatech.auth.jwtsecurity.infrastucture.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Validated
@Getter
@ConstructorBinding
@ConfigurationProperties(prefix = "jwt.token")
@RequiredArgsConstructor
public class JwtSetting {
    @NotNull
    private  final String secret;
    @NotNull
    private final Long secExpired;
}
