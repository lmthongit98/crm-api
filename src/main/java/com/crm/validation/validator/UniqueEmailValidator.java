package com.crm.validation.validator;

import com.crm.model.user.User;
import com.crm.repository.UserRepository;
import com.crm.common.util.ValidatorUtils;
import com.crm.validation.anotation.UniqueEmail;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {
    private String message;

    @Autowired
    private UserRepository userRepository;


    @Override
    public void initialize(UniqueEmail uniqueEmail) {
        message = uniqueEmail.message();
    }


    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        boolean isValid = userOpt.isEmpty();
        if (!isValid) {
            ValidatorUtils.addError(context, message);
        }
        return isValid;
    }
}
