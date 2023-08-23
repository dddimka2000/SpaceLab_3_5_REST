package org.example.util.UserValidatorAndConvert;

import org.example.dto.UserDTO;
import org.example.entity.UserEntity;
import org.example.service.UserDetailsServiceImpl;
import org.example.service.UserEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Component
public class UserValidator implements Validator {
    private final UserDetailsServiceImpl userDetailsService;

    private final
    UserEntityService userEntityService;

    @Autowired
    public UserValidator(UserDetailsServiceImpl userDetailsService, UserEntityService userEntityService) {
        this.userDetailsService = userDetailsService;
        this.userEntityService = userEntityService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return UserEntity.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserDTO user = (UserDTO) target;
        try {
            Optional<UserEntity> tryFindEmail = userEntityService.findByEmail(user.getEmail());
            if (tryFindEmail.isPresent()) {
                errors.rejectValue("email", "", "User with so email already have account");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try {
            Optional<UserEntity> tryFindTelephone = userEntityService.findByTelephone(user.getTelephone());
            if (tryFindTelephone.isPresent()) {
                errors.rejectValue("telephone", "", "User with so telephone already have account");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try {
            Optional<UserEntity> tryFindLogin = userEntityService.findByLogin(user.getLogin());
            if (tryFindLogin.isPresent()) {
                errors.rejectValue("login", "", "User with so login already have account");
            }
        } catch (Exception e) {
            return;
        }
    }
}
