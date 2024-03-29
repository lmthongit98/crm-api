package com.crm.validation.anotation;

import com.crm.validation.validator.UniqueGroupNameValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UniqueGroupNameValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface UniqueGroupName {
    String message() default "Group name is already exist.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
