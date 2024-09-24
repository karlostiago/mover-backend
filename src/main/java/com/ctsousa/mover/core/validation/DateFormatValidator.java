package com.ctsousa.mover.core.validation;

import com.ctsousa.mover.core.annotation.DateFormat;
import com.ctsousa.mover.core.util.DateUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

import static com.ctsousa.mover.core.util.DateUtil.isValidDate;

public class DateFormatValidator implements ConstraintValidator<DateFormat, Object> {

    @Override
    public void initialize(DateFormat constraintAnnotation) {

    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext constraintValidatorContext) {
        if (object == null) return true;

        boolean valid = false;

        if (object instanceof LocalDate localDate) {
            valid = isValidDate(localDate);
        }

        return valid;
    }
}
