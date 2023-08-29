package org.example.controller;


import org.example.entity.UserEntity;
import org.example.security.UserDetailsImpl;
import org.example.service.UserEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@RestControllerAdvice
public class GlobalAttributes {

    @ModelAttribute("loggedInUser")
    public Optional<UserDetailsImpl> getValueNameUser(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        if(userDetails!=null){
        return Optional.of(userDetails);}
        return Optional.empty();
    }
}
