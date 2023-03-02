package com.epam.esm.controller.constraint;

import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.springframework.util.MultiValueMap;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class FilterValidator implements ConstraintValidator<FilterConstraint, MultiValueMap<String, String>> {

    private static final String INVALID_DATE_SORT = "40007";
    private static final String INVALID_NAME_SORT = "40008";

    private static final String ASCENDING = "asc";
    private static final String DESCENDING = "desc";

    @Override
    public boolean isValid(MultiValueMap<String, String> stringStringMultiValueMap,
                           ConstraintValidatorContext context) {
        HibernateConstraintValidatorContext hibernateContext =
                context.unwrap( HibernateConstraintValidatorContext.class);
        hibernateContext.disableDefaultConstraintViolation();
        List<String> errors = new LinkedList<>();
        for (Map.Entry<String, List<String>> entry : stringStringMultiValueMap.entrySet()) {
            String filterKey = entry.getKey();
            String filterValue = entry.getValue().stream().findFirst().orElse("");
            switch (filterKey) {
                case "date_sort":
                    if (!(filterValue.equals(ASCENDING) || filterValue.equals(DESCENDING))) {
                        errors.add(filterKey);
                        hibernateContext.buildConstraintViolationWithTemplate(INVALID_DATE_SORT)
                                .addPropertyNode(filterValue)
                                .addConstraintViolation();
                    }
                    break;
                case "name_sort":
                    if (!(filterValue.equals(ASCENDING) || filterValue.equals(DESCENDING))) {
                        errors.add(filterKey);
                        hibernateContext.buildConstraintViolationWithTemplate(INVALID_NAME_SORT)
                                .addPropertyNode(filterValue)
                                .addConstraintViolation();
                    }
                    break;
                default:
                    break;
            }
        }
        return errors.isEmpty();
    }

}
