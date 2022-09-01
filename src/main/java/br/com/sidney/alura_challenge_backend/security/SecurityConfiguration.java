package br.com.sidney.alura_challenge_backend.security;

import br.com.sidney.alura_challenge_backend.service.JwtUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;

@EnableWebSecurity
@Profile("prod")
@Slf4j
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    private final JwtUserDetailsService userDetailsService;
    private final String secret;

    public SecurityConfiguration(JwtUserDetailsService userDetailsService,
                                 @Value("${jwt.secret}") String secret) {
        this.userDetailsService = userDetailsService;
        this.secret = secret;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.cors().configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues())
                .and().csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/login").permitAll()
                .antMatchers(HttpMethod.POST, "/api/v1/roles", "/api/v1/users").hasRole("ADMIN")
                .anyRequest()
                .authenticated()
                .and()
                .addFilter(new JwtAuthenticationFilter(authenticationManager(), secret))
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), userDetailsService, secret));
    }
}
