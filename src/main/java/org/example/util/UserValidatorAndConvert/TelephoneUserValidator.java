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
public class TelephoneUserValidator implements Validator {



    private final
    UserEntityService userEntityService;

    @Autowired
    public TelephoneUserValidator(UserEntityService userEntityService) {
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
            Optional<UserEntity> tryFindByTelephone = userEntityService.findByTelephone(user.getTelephone());
            if (tryFindByTelephone.isPresent()) {
                errors.rejectValue("telephone", "9291", "User with so telephone already have account");
            }
        } catch (Exception e) {
            return;
        }
    }


}
