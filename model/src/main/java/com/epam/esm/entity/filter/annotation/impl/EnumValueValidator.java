package com.epam.esm.entity.filter.annotation.impl;

import com.epam.esm.entity.filter.annotation.EnumValue;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EnumValueValidator implements ConstraintValidator<EnumValue, Enum<?>> {

    private Object[] enumValues;

    @Override
    public void initialize(final EnumValue annotation) {
        enumValues = annotation.enumClass().getEnumConstants();
    }

    @Override
    public boolean isValid(final Enum<?> value, final ConstraintValidatorContext context) {
        if (value == null) return true;
        String contextValue = value.toString();
        for (Object enumValue : enumValues) {
            if (enumValue.toString().equals(contextValue)) {
                return true;
            }
        }
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate("Can only be equal to 'ASC' or 'DESC' (case insensitive)")
                .addConstraintViolation();
        return false;
    }
}