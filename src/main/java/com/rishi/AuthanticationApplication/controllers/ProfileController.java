package com.rishi.AuthanticationApplication.controllers;

import com.rishi.AuthanticationApplication.dtos.CurrentUserResponse;
import com.rishi.AuthanticationApplication.model.Role;
import com.rishi.AuthanticationApplication.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class ProfileController {

    @GetMapping("/api/me")
    public ResponseEntity<CurrentUserResponse> me(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Set<String> roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toSet());
        return ResponseEntity.ok(new CurrentUserResponse(user.getName(), user.getEmail(), roles));
    }
}

