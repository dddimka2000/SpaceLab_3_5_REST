package org.example.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.example.dto.UserDTO;
import org.example.service.RegistrationService;
import org.example.util.UserValidatorAndConvert.PasswordValidator;
import org.example.util.UserValidatorAndConvert.UserDtoToEntity;
import org.example.util.UserValidatorAndConvert.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
@Log4j2
@Tag(name = "Registration без токена", description = "описание, бла-бла-бла")
public class RegistrationController {
    private final
    UserValidator userValidator;

    private final
    RegistrationService registrationService;

    final
    PasswordValidator passwordValidator;

    @Autowired
    public RegistrationController(UserValidator userValidator, RegistrationService registrationService, PasswordValidator passwordValidator) {
        this.userValidator = userValidator;
        this.registrationService = registrationService;
        this.passwordValidator = passwordValidator;
    }

    @GetMapping("/registration")
    public String registrationUserGet(@ModelAttribute("userEntity") UserDTO person) {
        return "/auth/registration";
    }

    @PostMapping("/registration")
    public String registrationUserPost(@ModelAttribute("userEntity") @Valid UserDTO userEntity, BindingResult bindingResult) {
        userValidator.validate(userEntity,bindingResult);
        passwordValidator.validate(userEntity,bindingResult);
        if (bindingResult.hasErrors()) {
            return "/auth/registration";
        }
        log.info(userEntity);
        log.info(bindingResult.getAllErrors());
        userEntity.setPath("/photos/01.png");
        registrationService.registration(new UserDtoToEntity().convert(userEntity));
        return "redirect:/auth/login";
    }


}
