package br.com.sidney.alura_challenge_backend.validators.interfaces;

import br.com.sidney.alura_challenge_backend.validators.DateValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = DateValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Date {

    String message() default "Is not a valid date";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
