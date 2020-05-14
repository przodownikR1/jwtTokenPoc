package pl.scalatech.auth.jwtsecurity.infrastucture.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import pl.scalatech.auth.jwtsecurity.port.UserRepositoryPort;

import javax.servlet.http.HttpServletRequest;

@Configuration
class SecurityConfig {

    @Bean
    JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint() {
        return new JwtAuthenticationEntryPoint();
    }

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    JwtTokenProvider jwtTokenProvider(JwtSetting jwtSetting) {
        return new JwtTokenProvider(jwtSetting);
    }

    @Bean
    UserDetailsService jwtUserDetailsService(UserRepositoryPort userRepo) {
        return new JwtUserDetailsService(userRepo);
    }

    @Bean
    JwtRequestFilter jwtRequestFilter(UserDetailsService jwtUserDetailsService, JwtTokenProvider tokenProvider) {
        return new JwtRequestFilter(jwtUserDetailsService, tokenProvider);
    }
}
