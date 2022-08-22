package br.com.sidney.alura_challenge_backend.validators;

import br.com.sidney.alura_challenge_backend.validators.interfaces.Month;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MonthValidator implements ConstraintValidator<Month, Integer> {
    /**
     * @param value
     * @param context
     * @return true if month is valid
     */
    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        return (value >= 1 && value <=12)
                && (value instanceof Integer)
                && (Integer.toString(value).length() >= 1 && Integer.toString(value).length() <=2);
    }
}
