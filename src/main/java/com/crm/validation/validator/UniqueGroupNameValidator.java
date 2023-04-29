package com.crm.validation.validator;

import com.crm.model.role.Group;
import com.crm.repository.GroupRepository;
import com.crm.util.ValidatorUtils;
import com.crm.validation.anotation.UniqueGroupName;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class UniqueGroupNameValidator implements ConstraintValidator<UniqueGroupName, String> {

    private String message;

    @Autowired
    private GroupRepository groupRepository;

    @Override
    public void initialize(UniqueGroupName constraintAnnotation) {
        this.message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        Optional<Group> groupOpt = groupRepository.findByName(value);
        boolean isValid = groupOpt.isEmpty();
        if (!isValid) {
            ValidatorUtils.addError(context, message);
        }
        return isValid;
    }
}
