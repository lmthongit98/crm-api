package com.crm.validation.validator;

import com.crm.model.role.Role;
import com.crm.repository.RoleRepository;
import com.crm.security.enums.SecurityAuthority;
import com.crm.util.ValidatorUtils;
import com.crm.validation.anotation.UniqueRoleName;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class UniqueRoleNameValidator implements ConstraintValidator<UniqueRoleName, SecurityAuthority> {

    private String message;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void initialize(UniqueRoleName constraintAnnotation) {
        this.message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(SecurityAuthority value, ConstraintValidatorContext context) {
        Optional<Role> roleOpt = roleRepository.findByName(value);
        boolean isValid = roleOpt.isEmpty();
        if (!isValid) {
            ValidatorUtils.addError(context, message);
        }
        return isValid;
    }
}
