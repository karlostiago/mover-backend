package com.ctsousa.mover.core.validation;

import com.ctsousa.mover.core.annotation.NotEmpty;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

public class NotEmptyValidator implements ConstraintValidator<NotEmpty, Object> {
    @Override
    public void initialize(NotEmpty constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
        if (value instanceof Long) {
            return !((Long) value == 0 || (Long) value < 0);
        }

        if (value instanceof Integer) {
            return !((Integer) value == 0 || (Integer) value < 0);
        }

        if (value == null) {
            return false;
        }

        if (value instanceof String string) {
            return !StringUtils.isBlank(string) && (!StringUtils.equalsIgnoreCase(string, "undefined"));
        }

        return true;
    }
}
