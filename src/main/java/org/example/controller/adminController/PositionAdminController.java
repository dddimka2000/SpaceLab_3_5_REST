package org.example.controller.adminController;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import org.example.entity.UserEntity;
import org.example.entity.UserRole;
import org.example.security.UserDetailsImpl;
import org.example.service.UserEntityService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Controller
@Log4j2
//@PreAuthorize("hasRole('ADMIN')")
@PreAuthorize("hasAuthority('ADMIN')")
@Tag(name = "Position Admin", description = "описание, бла-бла-бла")
public class PositionAdminController {
    final
    UserEntityService userEntityService;

    public PositionAdminController(UserEntityService userEntityService) {
        this.userEntityService = userEntityService;
    }

    int size = 3;

    @GetMapping("/admin/positions")
    public ResponseEntity<Map<String, Object>> adminPositionShow(@RequestParam(defaultValue = "0") int page) {
        Map<String, Object> response = new HashMap<>();
        Page<UserEntity> userPage = userEntityService.findAllUsersPage(page, size);
        List<UserEntity> users = userPage.getContent();
        List<String> roles = new ArrayList<>();
        getPage = page;
        users.forEach(userEntity -> roles.add(userEntity.getRoles().iterator().next().getAuthority()));
        response.put("roles", roles);
        response.put("users", users);
        response.put("currentPage", page);
        response.put("totalPages", userPage.getTotalPages());
        long count = userEntityService.countBy();
        String panelCount = "Показано " + (size * page + 1) + "-" + (users.size() + (size * page)) + " из " + count;
        log.info(panelCount);
        response.put("panelCount", panelCount);
        response.put("sizePageItems", size);
        return ResponseEntity.ok().body(response);
    }

    int getPage = 0;


    @PostMapping(value = "/admin/positions/update", consumes = "multipart/form-data")
    public ResponseEntity<String> changeRolesAdminPanel(@RequestParam Map<String, String> roles,
                                                        @AuthenticationPrincipal UserDetailsImpl userEntity) {
        log.info(roles);
        UserEntity user = null;
        List<String> stringList = new ArrayList<>();
        for (Map.Entry<String, String> entry : roles.entrySet()) {
            log.info(entry.getKey());
            if (!entry.getKey().equals("_csrf")) {
                if (!userEntity.getUserEntity().getLogin().equals(entry.getKey())) {
                    user = userEntityService.findByLogin(entry.getKey()).get();
                    Set<UserRole> userRoles = new HashSet<>();
                    switch (entry.getValue()) {
                        case "ADMIN":
                            userRoles.add(UserRole.ADMIN);
                            break;
                        case "MODERATOR":
                            userRoles.add(UserRole.MODERATOR);
                            break;
                        case "USER":
                            userRoles.add(UserRole.USER);
                            break;
                    }
                    if (user != null) {
                        user.setRoles(userRoles);
                        userEntityService.save(user);
                        stringList.add(user.getLogin());
                    }
                } else {
                    if (!entry.getValue().equals(userEntity.getUserEntity().getRoles().stream().iterator().next().getAuthority())) {
                        log.info(userEntity.getUserEntity().getRoles().stream().iterator().next().getAuthority());
                    }
                }
            }
        }
        return ResponseEntity.ok("Users" + stringList + " was updated");
    }
}