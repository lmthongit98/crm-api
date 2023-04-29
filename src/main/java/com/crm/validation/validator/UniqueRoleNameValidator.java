package com.crm.validation.validator;

import com.crm.model.role.Role;
import com.crm.repository.RoleRepository;
import com.crm.util.ValidatorUtils;
import com.crm.validation.anotation.UniqueRoleName;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class UniqueRoleNameValidator implements ConstraintValidator<UniqueRoleName, String> {

    private String message;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void initialize(UniqueRoleName constraintAnnotation) {
        this.message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        Optional<Role> roleOpt = roleRepository.findByName(value.toUpperCase());
        boolean isValid = roleOpt.isEmpty();
        if (!isValid) {
            ValidatorUtils.addError(context, message);
        }
        return isValid;
    }
}
