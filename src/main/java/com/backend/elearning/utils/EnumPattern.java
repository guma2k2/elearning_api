package com.backend.elearning.utils;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;


@Documented
@Constraint(validatedBy = EnumPatternValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface EnumPattern {
    String name();
    String regexp();
    String message() default "{name} must match {regexp}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
