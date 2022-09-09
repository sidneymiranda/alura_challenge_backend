package br.com.sidney.alura_challenge_backend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Profile("dev")
public class DevSecurityConfiguration {

    @Bean
    public SecurityFilterChain devFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeRequests()
                .antMatchers("/api/v1/**").permitAll()
                .and()
                .csrf().disable()
                .build();
    }
}
