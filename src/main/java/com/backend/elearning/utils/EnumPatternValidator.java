package com.backend.elearning.utils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;
public class EnumPatternValidator implements ConstraintValidator<EnumPattern, Enum<?>> {

    private Pattern pattern;

    @Override
    public void initialize(EnumPattern constraintAnnotation) {
        this.pattern = Pattern.compile(constraintAnnotation.regexp());
    }

    @Override
    public boolean isValid(Enum<?> value, ConstraintValidatorContext context) {
        if (value == null) return true;
        return pattern.matcher(value.name()).matches();
    }
}