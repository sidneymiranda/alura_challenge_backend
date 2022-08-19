package br.com.sidney.alura_challenge_backend.validators.interfaces;

import br.com.sidney.alura_challenge_backend.validators.YearValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = YearValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Year {

    String message() default "Not a valid year";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
