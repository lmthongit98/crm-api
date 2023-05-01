package com.crm.validation.validator;

import com.crm.model.user.User;
import com.crm.repository.UserRepository;
import com.crm.common.util.ValidatorUtils;
import com.crm.validation.anotation.UniqueUsername;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String> {
    private String message;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void initialize(UniqueUsername uniqueUsername) {
        message = uniqueUsername.message();
    }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        boolean isValid = userOpt.isEmpty();
        if (!isValid) {
            ValidatorUtils.addError(context, message);
        }
        return isValid;
    }

}
