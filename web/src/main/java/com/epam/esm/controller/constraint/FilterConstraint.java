package com.epam.esm.controller.constraint;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = FilterValidator.class)
@Target( {ElementType.PARAMETER, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface FilterConstraint {
    String message() default "Invalid filter param";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
