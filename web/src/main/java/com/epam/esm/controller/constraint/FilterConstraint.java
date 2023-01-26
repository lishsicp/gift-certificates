package com.epam.esm.controller.constraint;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = FilterValidator.class)
@Target( {ElementType.PARAMETER, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface FilterConstraint {
    String message() default "Invalid filter param";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
