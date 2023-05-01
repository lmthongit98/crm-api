package com.crm.common.util;

import jakarta.validation.ConstraintValidatorContext;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ValidatorUtils {
    public void addError(ConstraintValidatorContext context,
                                String message) {
        context.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation()
                .disableDefaultConstraintViolation();
    }
}
