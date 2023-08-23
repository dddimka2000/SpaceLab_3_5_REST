package org.example.service;

import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.example.entity.UserEntity;
import org.example.repository.UserRepository;
import org.example.security.UserDetailsImpl;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Pattern;

@Log4j2
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("UserDetailsServiceImpl-loadUserByUsername start");
        if (username == null) {
            username = "";
        }
        if (username.isEmpty() || username == "") {
            log.error("String empty!");
        }
        Optional<UserEntity> user;
        if (username.contains("@") && username.contains(".")) {
            user = userRepository.findByEmail(username);
            log.info("Try entry by E-mail");
        } else if (Pattern.matches("[+0-9]+", username) && username.contains("+")) {
            user = userRepository.findByTelephone(username);
            log.info("Try entry by telephone");
        } else {
            log.info("Try entry by login");
            user = userRepository.findByLogin(username);
        }

        if (!user.isPresent()) {
            throw new UsernameNotFoundException("User hasn't been found!");
        } else {
            log.info(username + " has been found");
            log.info(username + " have roles " + user.get().getRoles());
        }
        log.info("UserDetailsServiceImpl-loadUserByUsername end");
//        Hibernate.initialize(user.get().getBasketItemEntities());
//        Hibernate.initialize(user.get().getOrderTableEntities());
        return new UserDetailsImpl(user.get());
    }

}
