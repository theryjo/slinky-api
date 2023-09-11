package com.theryjo.slinky.config;

import com.theryjo.slinky.security.Authorities;
import com.theryjo.slinky.security.JWTAuthenticationFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import static org.springframework.security.config.Customizer.withDefaults;

@EnableWebSecurity
public class MultiHttpSecurityConfig {

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.builder()
                .username("demo")
                .password("{bcrypt}$2a$10$DYr2LQv6sMKlSH8/AUzo0.J4kJX4jTFWvK9/6r8IA2F1c2Ed5iIa.")
                .roles(Authorities.ROLE_API)
                .build();
        return new InMemoryUserDetailsManager(user);
    }

    @Configuration
    @Order(1)
    public static class SigninWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.cors().and().csrf().disable()
                    .antMatcher("/signin")
                    .authorizeRequests(authorize -> authorize
                            .anyRequest().authenticated()
                    )
                    .httpBasic(withDefaults());
        }
    }

    @Configuration
    public static class ApiWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {
        @Value("${jwt.secret}")
        private String jwtSecret;

        protected void configure(HttpSecurity http) throws Exception {
            http.cors().and().csrf().disable()
                    .antMatcher("/api/**")
                    .authorizeRequests(authorize -> authorize
                            .anyRequest().hasRole(Authorities.ROLE_API)
                    )
                    .addFilter(new JWTAuthenticationFilter(authenticationManager(), jwtSecret))
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        }
    }
}
