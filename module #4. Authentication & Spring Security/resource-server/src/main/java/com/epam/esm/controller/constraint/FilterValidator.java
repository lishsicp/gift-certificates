package com.epam.esm.controller.constraint;

import org.springframework.util.MultiValueMap;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FilterValidator implements ConstraintValidator<FilterConstraint, MultiValueMap<String, String>> {

    private static final String DATE_SORT_KEY = "date_sort";
    private static final String NAME_SORT_KEY = "name_sort";
    private static final String SORT_ERROR_CODE = "40007";
    private static final String ASCENDING = "asc";
    private static final String DESCENDING = "desc";

    @Override
    public boolean isValid(MultiValueMap<String, String> filterParams,
        ConstraintValidatorContext context) {
        return filterParams.toSingleValueMap()
            .entrySet()
            .stream()
            .filter(entry -> DATE_SORT_KEY.equals(entry.getKey()) || NAME_SORT_KEY.equals(entry.getKey()))
            .filter(entry -> !validateSortValue(context, entry.getValue()))
            .count() == 0; // can't use noneMatch as it leaves other values unchecked
    }

    boolean validateSortValue(ConstraintValidatorContext context, String value) {
        if (!(value.equals(ASCENDING) || value.equals(DESCENDING))) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(SORT_ERROR_CODE)
                .addPropertyNode(value)
                .addConstraintViolation();
            return false;
        }
        return true;
    }

}
