package org.example.controller.adminController;

import lombok.extern.log4j.Log4j2;
import org.example.dto.UploadImg;
import org.example.dto.UserDTO;
import org.example.entity.UserEntity;
import org.example.entity.UserRole;
import org.example.service.UserEntityService;
import org.example.util.UserValidatorAndConvert.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Log4j2
@RestController
public class UsersAdminController {
    @Value("${spring.regex}")
    String regex;
    @Value("${spring.pathImg}")
    String path;
    private final
    PhotoValidator photoValidator;
    private final
    UserEntityService userEntityService;
    final
    PasswordValidator passwordValidator;
    final
    UserValidator userValidator;
    private final
    EmailUserValidator emailUserValidator;

    private final
    LoginUserValidator loginUserValidator;
    private final
    TelephoneUserValidator telephoneUserValidator;
    final
    UserDtoToEntity userDtoToEntity;

    @Autowired
    public UsersAdminController(UserEntityService userEntityService, PhotoValidator photoValidator, PasswordValidator passwordValidator, UserValidator userValidator, EmailUserValidator emailUserValidator, LoginUserValidator loginUserValidator, TelephoneUserValidator telephoneUserValidator, UserDtoToEntity userDtoToEntity) {
        this.userEntityService = userEntityService;
        this.photoValidator = photoValidator;
        this.passwordValidator = passwordValidator;
        this.userValidator = userValidator;
        this.emailUserValidator = emailUserValidator;
        this.loginUserValidator = loginUserValidator;
        this.telephoneUserValidator = telephoneUserValidator;
        this.userDtoToEntity = userDtoToEntity;
    }

    @GetMapping("/admin/users")
    public ResponseEntity<Map<String, Object>> showAllUsersAdmin(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "sort", defaultValue = "id,asc") String sort,
            @RequestParam(name = "search", required = false) String search
    ) {
        Map<String, Object> response = new HashMap<>();
        Page<UserEntity> userPage = userEntityService.findPageAllUsersBySearchName(search, sort, page, size);
        List<UserEntity> users = userPage.getContent();
        response.put("users", users);
        response.put("currentPage", page);
        response.put("totalPages", userPage.getTotalPages());
        long count = userEntityService.countBy();
        String panelCount = "Показано " + (size * page + 1) + "-" + (size + (size * page)) + " из " + count;
        log.info(panelCount);
        response.put("panelCount", panelCount);
        response.put("sizeItems", size);
       return ResponseEntity.ok().body(response);

    }

    @GetMapping("/admin/users/{id}")
    public ResponseEntity<Map<String, Object>> showUserAdminEditPanel(@PathVariable Integer id, Authentication authentication, RedirectAttributes redirectAttributes,
                                         @ModelAttribute("userEntity") UserDTO userDTO, @ModelAttribute("uploadFile") UploadImg img) {
        Map<String, Object> response = new HashMap<>();
        log.info(authentication.getAuthorities());
        if (userEntityService.findById(id).get().getRoles().contains(UserRole.ADMIN)) {
            response.put("errorRedirect", "Вы не имеете доступа к редакции пользователя с ролью \"Admin\"");
            return ResponseEntity.badRequest().body(response);
        }
        UserEntity userEntity = userEntityService.findById(id).get();
        userDTO.setLogin(userEntity.getLogin());
        userDTO.setPass(userEntity.getPass());
        userDTO.setName(userEntity.getName());
        userDTO.setSurname(userEntity.getSurname());
        userDTO.setTelephone(userEntity.getTelephone());
        userDTO.setEmail(userEntity.getEmail());
        userDTO.setPath(userEntity.getPath());
        response.put("userEntity", userEntity);
        return ResponseEntity.ok().body(response);
    }

    int size = 2;

    @DeleteMapping("/admin/users/delete")
    public ResponseEntity<String> deleteProductAdmin(@RequestParam("id") Integer id) {
        Optional<UserEntity> userEntity = userEntityService.findById(id);
        if (userEntity.isPresent()) {
            log.info(userEntity.get().getRoles().contains(UserRole.USER));
            log.info(userEntity.get().getRoles().size());

            if (userEntity.get().getRoles().contains(UserRole.USER) && userEntity.get().getRoles().size() < 2 || userEntity.get().getRoles().size() == 0) {
                userEntityService.delete(userEntity.get());
                return ResponseEntity.ok("Пользователь успешно удален");
            } else {
                return ResponseEntity.ok("Нельзя удалить пользователя с какой-либо ролью, кроме \"USER\"");
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @ResponseBody
    @PutMapping(value = "/admin/users/{id}/uploadInfo", consumes = "multipart/form-data")
    public ResponseEntity<Map<String, Object>> adminUsersEditInfoPut(@ModelAttribute("userEntity")
                                                                 @Valid UserDTO userDTO,
                                                                 BindingResult bindingResult,
                                                                     @RequestParam("passRepeat") Optional<String> passwordRepeat,
                                                                     @RequestParam("pass") Optional<String> password,
                                                                     @PathVariable Integer id) {

        Map<String, Object> response = new HashMap<>();
        log.info(bindingResult.getFieldErrors());
        log.info("adminProductsEditPost+start");
        UserEntity userDetails=userEntityService.findById(id).get();
        log.info("length pass "+passwordRepeat.get().length());

        if (passwordRepeat.get().length()>=1 || password.get().length()>=1) {
            userDTO.setPass(password.get());
            passwordValidator.validate(userDTO, bindingResult);
            log.info("passwordRepeat.isPresent()||password.isPresent()");
            log.info(passwordRepeat.get());
            log.info(password.get());
            if (!password.get().equals(passwordRepeat.get())) {
                bindingResult.rejectValue("pass", "", "Пароли не совпадают");
            }
        }
        log.info("updateUserPublic" + userDTO);
        if (!userDetails.getEmail().equals(userDTO.getEmail())) {
            emailUserValidator.validate(userDTO, bindingResult);
        }
        if (!userDetails.getTelephone().equals(userDTO.getTelephone())) {
            telephoneUserValidator.validate(userDTO, bindingResult);
        }
        if (!userDetails.getLogin().equals(userDTO.getLogin())) {
            loginUserValidator.validate(userDTO, bindingResult);
        }
        if (bindingResult.hasErrors()) {
            Map<String, String> fieldErrors = new HashMap<>();
            log.error("updateUserPublic-PersonalAccountController error");
            for (FieldError error : bindingResult.getFieldErrors()) {
                fieldErrors.put(error.getField(), error.getDefaultMessage());
            }
            fieldErrors.forEach((s, s2) -> log.info(s + " = " + s2));
            response.put("fields", fieldErrors);
            return ResponseEntity.badRequest().body(response);
        }
        log.info("updateUserPublic" + userDTO);
        userDetails.setEmail(userDTO.getEmail());
        userDetails.setLogin(userDTO.getLogin());
        userDetails.setName(userDTO.getName());
        userDetails.setSurname(userDTO.getSurname());
        userDetails.setTelephone(userDTO.getTelephone());

        userEntityService.save(userDetails);
        return ResponseEntity.ok().body(response);
    }


    @PutMapping(value = "/admin/users/{id}/uploadImg", consumes = "multipart/form-data")
    public ResponseEntity<Map<String, Object>> adminUsersEditPut(@ModelAttribute("uploadFile")
                                                                     @Valid UploadImg uploadImg,
                                                                     BindingResult bindingResult, @PathVariable Integer id) {
        Map<String, Object> response = new HashMap<>();
        log.info(bindingResult.getFieldErrors());
        log.info("adminProductsEditPost+start");

        photoValidator.validate(uploadImg,bindingResult);
        if (bindingResult.hasErrors()) {
            ClassificationAdminController.putErrors(bindingResult, response, log);
            return ResponseEntity.badRequest().body(response);
        }

        UserEntity userEntity=userEntityService.findById(id).get();
        String mainImaginePath=userEntity.getPath();
        if (uploadImg.getFile() != null && !uploadImg.getFile().isEmpty()) {
            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "." + uploadImg.getFile().getOriginalFilename();
            try {
                uploadImg.getFile().transferTo(new File(path + resultFilename));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            mainImaginePath = regex + resultFilename;
            userEntity.setPath(mainImaginePath);
        }
        userEntityService.save(userEntity);
        response.put("newPath", mainImaginePath.substring(1));
        return ResponseEntity.ok().body(response);
    }
}





