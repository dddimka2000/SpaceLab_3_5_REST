package org.example.controller;

import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Logger;
import org.example.controller.adminController.ClassificationAdminController;
import org.example.dto.UploadImg;
import org.example.dto.UserDTO;
import org.example.entity.UserEntity;
import org.example.security.UserDetailsImpl;
import org.example.service.UserEntityService;
import org.example.util.UserValidatorAndConvert.*;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.io.File;
import java.io.IOException;
import java.util.*;

@RestController
@Log4j2
@RequestMapping("/personal_account")
public class PersonalAccountController {
    private final
    UserValidator userValidator;

    private final
    EmailUserValidator emailUserValidator;

    private final
    LoginUserValidator loginUserValidator;
    private final
    TelephoneUserValidator telephoneUserValidator;
    private final
    PasswordValidator passwordValidator;

    private final
    UserEntityService userEntityService;
    private final
    UserDtoToEntity userDtoToEntity;
    private final
    PhotoValidator photoValidator;

    @Autowired
    public PersonalAccountController(UserValidator userValidator, UserEntityService userEntityService, UserDtoToEntity userDtoToEntity, EmailUserValidator emailUserValidator, LoginUserValidator loginUserValidator, TelephoneUserValidator telephoneUserValidator, PasswordValidator passwordValidator, PhotoValidator photoValidator) {
        this.userValidator = userValidator;
        this.userEntityService = userEntityService;
        this.userDtoToEntity = userDtoToEntity;
        this.emailUserValidator = emailUserValidator;
        this.loginUserValidator = loginUserValidator;
        this.telephoneUserValidator = telephoneUserValidator;
        this.passwordValidator = passwordValidator;
        this.photoValidator = photoValidator;
    }

    @Value("${spring.regex}")
    String regex;
    @Value("${spring.pathImg}")
    String path;

    @GetMapping
    public ResponseEntity<Map<String, Object>> showPersonalAccount(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        UserDTO person = new UserDTO();
        Map<String, Object> response = new HashMap<>();
        UserEntity userEntity = userEntityService.findByLogin(userDetails.getUsername()).get();
        person.setPath(userEntity.getPath());
        person.setEmail(userEntity.getEmail());
        person.setLogin(userEntity.getLogin());
        person.setName(userEntity.getName());
        person.setSurname(userEntity.getSurname());
        person.setTelephone(userEntity.getTelephone());
        response.put("person", person);

        return ResponseEntity.ok().body(response);
    }

    @PostMapping(value = "/uploader/path", consumes = "multipart/form-data")
    public ResponseEntity<Map<String, Object>> updateUserPublicPath(@ModelAttribute("uploadFile") @Valid UploadImg uploadFile,
                                                                    BindingResult bindingResult,
                                                                    @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Map<String, Object> response = new HashMap<>();
        photoValidator.validate(uploadFile, bindingResult);
        if (bindingResult.hasErrors()) {
            PersonalAccountController.putErrors(bindingResult, response, log);
            return ResponseEntity.badRequest().body(response);
        }
        String uuidFile = UUID.randomUUID().toString();
        String resultFilename = uuidFile + "." + uploadFile.getFile().getOriginalFilename();
        try {
            uploadFile.getFile().transferTo(new File(path + resultFilename));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String mainImaginePath = regex + resultFilename;
        UserEntity userEntity = userDetails.getUserEntity();
        userEntity.setPath(mainImaginePath);
        userEntityService.save(userEntity);
        response.put("userEntity",userEntity);
        return ResponseEntity.ok().body(response);
    }
    public static void putErrors(BindingResult bindingResult, Map<String, Object> response, Logger log) {
        Map<String, String> fieldErrors = new HashMap<>();
        for (FieldError error : bindingResult.getFieldErrors()) {
            fieldErrors.put(error.getField(), error.getDefaultMessage());
        }
        fieldErrors.forEach((s, s2) -> log.info(s + " = " + s2));
        response.put("fields", fieldErrors);
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<Map<String, Object>> updateUserPublic(@ModelAttribute("userEntity") @Valid UserDTO userDTO, BindingResult bindingResult,
                                                                @AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                @RequestParam("passwordRepeat") Optional<String> passwordRepeat,
                                                                @RequestParam("password") Optional<String> password,
                                                                Errors errors,
                                                                @ModelAttribute("uploadFile") UploadImg uploadFile) {
        Map<String, Object> response = new HashMap<>();
        userDTO.setPass(userDetails.getPassword());
        if (!passwordRepeat.get().isBlank() || !password.get().isBlank()) {
            userDTO.setPass(password.get());
            passwordValidator.validate(userDTO, errors);
            log.info("passwordRepeat.isPresent()||password.isPresent()");
            log.info(passwordRepeat.get());
            log.info(password.get());
            if (!password.get().equals(passwordRepeat.get())) {
                response.put("passwordRepeat", false);
                bindingResult.rejectValue("pass", "", "Пароли не совпадают");
            }

        }

        log.info("updateUserPublic" + userDTO);
        if (!userDetails.getUserEntity().getEmail().equals(userDTO.getEmail())) {
            emailUserValidator.validate(userDTO, bindingResult);
        }
        if (!userDetails.getUserEntity().getTelephone().equals(userDTO.getTelephone())) {
            telephoneUserValidator.validate(userDTO, bindingResult);
        }
        if (!userDetails.getUserEntity().getLogin().equals(userDTO.getLogin())) {
            loginUserValidator.validate(userDTO, bindingResult);
        }
        if (bindingResult.hasErrors()) {
            password = Optional.of("");
            passwordRepeat = Optional.of("");
            log.error("updateUserPublic-PersonalAccountController error");
            PersonalAccountController.putErrors(bindingResult, response, log);
            return ResponseEntity.badRequest().body(response);
        }
        UserEntity userEntity = userDtoToEntity.convert(userDTO, userDetails.getUserEntity());
        userEntityService.save(userEntity);
        response.put("userEntity",userEntity);
        if (!password.get().isBlank()) {
            response.put("changePass", true);
        }
        return ResponseEntity.ok().body(response);
    }
}
