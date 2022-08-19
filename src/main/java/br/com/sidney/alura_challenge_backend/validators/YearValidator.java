package br.com.sidney.alura_challenge_backend.validators;

import br.com.sidney.alura_challenge_backend.validators.interfaces.Year;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class YearValidator implements ConstraintValidator<Year, Integer> {
    /**
     * @param value
     * @param context
     * @return
     */
    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        return value != null
                && !Integer.toString(value).isBlank()
                && !Integer.toString(value).isEmpty()
                && (value instanceof Integer)
                && (Integer.toString(value).length() == 4);
    }
}
