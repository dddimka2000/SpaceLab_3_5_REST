package org.example.util.UserValidatorAndConvert;

import org.example.dto.UserDTO;
import org.example.entity.UserEntity;
import org.example.service.UserDetailsServiceImpl;
import org.example.service.UserEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Component
public class EmailUserValidator implements Validator {

    private final
    UserEntityService userEntityService;

    @Autowired
    public EmailUserValidator(UserEntityService userEntityService) {
        this.userEntityService = userEntityService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return UserDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserDTO user = (UserDTO) target;
        try {
            Optional<UserEntity> tryFindEmail = userEntityService.findByEmail(user.getEmail());
            if (tryFindEmail.isPresent()) {
                errors.rejectValue("email", "9290", "User with so email already have account");
            }
        } catch (Exception e) {
            return;
        }
    }

}
