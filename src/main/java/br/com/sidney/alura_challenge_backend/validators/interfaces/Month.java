package br.com.sidney.alura_challenge_backend.validators.interfaces;

import br.com.sidney.alura_challenge_backend.validators.MonthValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = MonthValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Month {

    String message() default "Not a valid month";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
