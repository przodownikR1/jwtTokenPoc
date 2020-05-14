package pl.scalatech.auth.jwtsecurity.infrastucture.security;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Collections;

import static org.springframework.http.HttpMethod.OPTIONS;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;
import static pl.scalatech.auth.jwtsecurity.infrastucture.security.AuthoritiesConstants.ADMIN;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
//@EnableGlobalMethodSecurity(prePostEnabled = true)
class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserDetailsService jwtUserDetailsService;
    private final JwtRequestFilter jwtRequestFilter;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                // .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
                .and()
                .sessionManagement()
                .sessionCreationPolicy(STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/ops/health").permitAll()
                .antMatchers("/ops/info").permitAll()
                .antMatchers("/ops/configprops").hasAuthority(ADMIN)
                .antMatchers("/api/manager").hasAuthority(AuthoritiesConstants.MANAGER)
                .antMatchers("/api/user").hasAuthority(AuthoritiesConstants.USER)
                .antMatchers("/api/authenticate")
                .permitAll().antMatchers("/api/register")
                .permitAll()
                .anyRequest()
                .fullyAuthenticated()
                .and()
                .httpBasic();

    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(jwtUserDetailsService)
            .passwordEncoder(passwordEncoder);
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
           .antMatchers(OPTIONS, "/**")
           .antMatchers("/app/**/*.{js,html}")
           .antMatchers("/i18n/**")
           .antMatchers("/content/**")
           .antMatchers("/test/**")
           .antMatchers("/swagger-ui.html", "/swagger-resources/**", "/webjars/**", "/v2/api-docs");
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    @Bean
    public FilterRegistrationBean corsFilter() {
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(corsConfig()));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }

    @Bean
    public CorsConfigurationSource corsConfig() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList("*"));
        configuration.setAllowedMethods(Collections.singletonList("*"));
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
