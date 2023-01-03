package com.epam.esm.entity.filter.annotation;
import com.epam.esm.entity.filter.annotation.impl.EnumValueValidator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;

@Documented
@Constraint(validatedBy = { EnumValueValidator.class })
@Retention(RetentionPolicy.RUNTIME)
@ReportAsSingleViolation
@Target(ElementType.FIELD)
public @interface EnumValue {
    String                             message() default "{validation.enum.message}";
    Class<?>[]                         groups()  default {};
    Class<? extends Payload>[]         payload() default {};
    Class<? extends java.lang.Enum<?>> enumClass();
}