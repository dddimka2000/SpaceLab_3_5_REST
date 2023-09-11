package org.example.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.dto.AuthenticationRequestDto;
import org.example.entity.UserEntity;
import org.example.security.jwt.AuthenticationRequest;
import org.example.security.jwt.AuthenticationResponse;
import org.example.security.jwt.AuthenticationService;
import org.example.security.jwt.RegisterRequest;
import org.example.service.UserDetailsServiceImpl;
import org.example.service.UserEntityService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;

//@Log4j2
//@RestController
//@RequestMapping(value = "/api/v1/auth/")
//public class AuthenticationController {
//
//
//    private final AuthenticationManager authenticationManager;
//
//    private final UserDetailsServiceImpl userDetailsService;
//
//    public AuthenticationController(AuthenticationManager authenticationManager, UserDetailsServiceImpl userDetailsService, UserEntityService userService) {
//        this.authenticationManager = authenticationManager;
//        this.userDetailsService = userDetailsService;
//        this.userService = userService;
//    }
//
//    private final UserEntityService userService;
//
////        @PostMapping("/authenticate")
////    public AuthenticationResponse createAuthenticationToken(@RequestBody AuthenticationDTO authenticationDTO, HttpServletResponse response) throws BadCredentialsException, DisabledException, UsernameNotFoundException, IOException {
////        try {
////            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationDTO.getEmail(), authenticationDTO.getPassword()));
////        } catch (BadCredentialsException e) {
////            throw new BadCredentialsException("Incorrect username or password!");
////        } catch (DisabledException disabledException) {
////            response.sendError(HttpServletResponse.SC_NOT_FOUND, "User is not activated");
////            return null;
////        }
////
////        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationDTO.getEmail());
////
////        final String jwt = jwtUtil.generateToken(userDetails.getUsername());
////
////
////
////        return new AuthenticationResponse(jwt);
////
////    }
////    private final AuthenticationService service;
////
////    @PostMapping("/authenticate")
////    public ResponseEntity<AuthenticationResponse> authenticate(
////            @RequestBody AuthenticationRequest request
////    ) {
////        log.info(request.toString());
////        return ResponseEntity.ok(service.authenticate(request));
////    }
////
////    @PostMapping("/refresh-token")
////    public void refreshToken(
////            HttpServletRequest request,
////            HttpServletResponse response
////    ) throws IOException {
////        service.refreshToken(request, response);
////    }
//
//}

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "описание, бла-бла-бла")
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(service.register(request));
    }
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(HttpServletRequest request2,
            @RequestBody AuthenticationRequest request
    ) {
        removeAuthorizationHeader(request2);
        return ResponseEntity.ok(service.authenticate(request));
    }

//    @PostMapping("/refresh-token")
//    public ResponseEntity<String> refreshToken(
//            HttpServletRequest request,
//            HttpServletResponse response
//    ) throws IOException {
//        String token=service.refreshToken(request, response);
//        return ResponseEntity.ok(token);
//    }
    public HttpServletRequest removeAuthorizationHeader(HttpServletRequest request) {
        return new HttpServletRequestWrapper(request) {
            @Override
            public String getHeader(String name) {
                if ("Authorization".equalsIgnoreCase(name)) {
                    // Возвращаем null, чтобы удалить заголовок "Authorization"
                    return null;
                }
                return super.getHeader(name);
            }

            @Override
            public Enumeration<String> getHeaders(String name) {
                if ("Authorization".equalsIgnoreCase(name)) {
                    // Возвращаем пустую перечисление, чтобы удалить заголовок "Authorization"
                    return Collections.emptyEnumeration();
                }
                return super.getHeaders(name);
            }
        };
    }

}