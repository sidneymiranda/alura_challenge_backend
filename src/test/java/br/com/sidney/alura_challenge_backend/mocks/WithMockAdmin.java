package br.com.sidney.alura_challenge_backend.mocks;

import org.springframework.security.test.context.support.WithMockUser;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithMockUser(username = "admin", roles = {"SUPER_ADMIN"})
public @interface WithMockAdmin { }
