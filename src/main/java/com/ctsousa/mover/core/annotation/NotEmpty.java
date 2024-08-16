package com.ctsousa.mover.core.annotation;

import com.ctsousa.mover.core.validation.NotEmptyValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = NotEmptyValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface NotEmpty {
    String message() default "O campo não pode ser nulo ou vazio";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
