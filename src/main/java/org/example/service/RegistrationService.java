package org.example.service;

import lombok.extern.log4j.Log4j2;
import org.example.entity.UserEntity;
import org.example.entity.UserRole;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Log4j2
@Service
public class RegistrationService {
    final
    UserRepository userRepository;

    public RegistrationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void registration(UserEntity user){
        log.info("UserEntity-registration");
        user.setRoles(Collections.singleton(UserRole.USER));
        userRepository.save(user);
        log.info("UserEntity-registration successful");
    }

}
