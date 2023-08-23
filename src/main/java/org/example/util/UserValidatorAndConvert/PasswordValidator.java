package org.example.util.UserValidatorAndConvert;

import lombok.extern.log4j.Log4j2;
import org.example.dto.UserDTO;
import org.example.entity.UserEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Component
@Log4j2
public class PasswordValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return UserDTO.class.equals(clazz);
    }


    @Override
    public void validate(Object target, Errors errors) {
        UserDTO user = (UserDTO) target;
        try {
            String pass=user.getPass();
            char[] characters=pass.toCharArray();
            log.warn(characters.length+" char pass length");
            if (characters.length<8||characters.length>30) {
                errors.rejectValue("pass", "", "Пароль должен быть от 8 до 30 символов");
            }
        } catch (Exception e) {
            return;
        }
    }
}
