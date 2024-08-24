package com.ctsousa.mover.core.validation;

import com.ctsousa.mover.core.annotation.DateFormat;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class DateFormatValidator implements ConstraintValidator<DateFormat, Object> {

    private int minYear;
    private int maxYear;
    private int minMonth;
    private int maxMonth;
    private int minDay;
    private int maxDay;

    @Override
    public void initialize(DateFormat constraintAnnotation) {
        this.maxYear = LocalDate.now().getYear();
        this.minYear = 1900;
        this.minMonth = 1;
        this.maxMonth = 12;
        this.minDay = 1;
        this.maxDay = 31;
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext constraintValidatorContext) {
        if (object == null) return true;

        boolean valid = false;

        if (object instanceof LocalDate localDate) {
            valid =  isValidYear(localDate) && isValidMonth(localDate) && isValidDay(localDate);
        }

        return valid;
    }

    private boolean isValidYear(LocalDate localDate) {
        int year = localDate.getYear();
        return  year >= this.minYear && year <= this.maxYear;
    }

    private boolean isValidMonth(LocalDate localDate) {
        int month = localDate.getMonth().getValue();
        return month >= minMonth && month <= maxMonth;
    }

    private boolean isValidDay(LocalDate localDate) {
        int day = localDate.getDayOfMonth();
        return day >= minDay && day <= maxDay;
    }
}
