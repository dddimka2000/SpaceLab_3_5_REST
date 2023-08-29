package org.example.controller.adminController;

import lombok.extern.log4j.Log4j2;
import org.example.entity.FavoriteProductEntity;
import org.example.entity.UserEntity;
import org.example.entity.UserRole;
import org.example.service.FavoriteProductService;
import org.example.service.UserEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@Log4j2
public class MainAdminPage {
    private final
    UserEntityService userEntityService;

    final
    FavoriteProductService favoriteProductService;

    public MainAdminPage(UserEntityService userEntityService, FavoriteProductService favoriteProductService) {
        this.userEntityService = userEntityService;
        this.favoriteProductService = favoriteProductService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> showMainAdminPage() {
        Map<String, Object> response = new HashMap<>();
        List<UserEntity> list = userEntityService.findAllUsers();
        long userSize = list.stream().filter(userEntity -> userEntity.getRoles().contains(UserRole.USER)).count();
        long moderatorSize = list.stream().filter(userEntity -> userEntity.getRoles().contains(UserRole.MODERATOR)).count();
        long adminSize = list.stream().filter(userEntity -> userEntity.getRoles().contains(UserRole.ADMIN)).count();
        response.put("userSize", userSize);
        response.put("moderatorSize", moderatorSize);
        response.put("adminSize", adminSize);
        response.put("findBestSeven", favoriteProductService.findBestSeven());
        log.info(favoriteProductService.findBestSeven());
        return ResponseEntity.ok().body(response);
    }
}
