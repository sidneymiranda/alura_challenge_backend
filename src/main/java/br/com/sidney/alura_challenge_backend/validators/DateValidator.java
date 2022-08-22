package br.com.sidney.alura_challenge_backend.validators;

import br.com.sidney.alura_challenge_backend.validators.interfaces.Date;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class DateValidator implements ConstraintValidator<Date, String> {
    /**
     * @param date
     * @param constraintValidatorContext
     * @return true if date is valid
     */
    @Override
    public boolean isValid(String date, ConstraintValidatorContext constraintValidatorContext) {
        String matchingTheFebruary29Th = "^(29/02/(2000|2400|2800|(19|2[0-9])(0[48]|[2468][048]|[13579][26])))$";
        String matchingGeneralDaysOfFebruary = "^((0[1-9]|1[0-9]|2[0-8])/02/((19|2[0-9])[0-9]{2}))$";
        String matching30DayMonths = "^((0[1-9]|[12][0-9]|30)/(0[469]|11)/((19|2[0-9])[0-9]{2}))$";
        String matching31DayMonths = "^((0[1-9]|[12][0-9]|3[01])/(0[13578]|10|12)/((19|2[0-9])[0-9]{2}))$";
        String or = "|";

        StringBuilder patterns = new StringBuilder();
        patterns.append(matchingTheFebruary29Th)
                .append(or).append(matchingGeneralDaysOfFebruary)
                .append(or).append(matching30DayMonths)
                .append(or).append(matching31DayMonths);

        return date != null
                && Pattern.compile(patterns.toString()).matcher(date).matches();
    }
}
