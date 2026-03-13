package com.rishi.AuthanticationApplication.controllers;

import com.rishi.AuthanticationApplication.dtos.*;
import com.rishi.AuthanticationApplication.model.Role;
import com.rishi.AuthanticationApplication.model.User;
import com.rishi.AuthanticationApplication.repository.RoleRepository;
import com.rishi.AuthanticationApplication.repository.UserRepository;
import com.rishi.AuthanticationApplication.security.JwtService;
import com.rishi.AuthanticationApplication.security.TokenBlacklistService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final TokenBlacklistService tokenBlacklistService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request) {

        if (userRepository.existsByEmail(request.email())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new RegisterResponse(
                            request.email(),
                            "Email already in use"
                    ));
        }

        String roleName = request.role().toUpperCase();
        Role role = roleRepository
                .findByName(roleName)
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setName(roleName);
                    return roleRepository.save(newRole);
                });

        User user = User.builder()
                .name(request.name())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .enable(true)
                .build();
        user.getRoles().add(role);

        User savedUser = userRepository.save(user);

        RegisterResponse response = new RegisterResponse(
                savedUser.getEmail(),
                "User registered successfully"
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        User user = (User) authentication.getPrincipal();

        String token = jwtService.generateToken(user);

        Set<String> roles = user.getRoles()
                .stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        AuthResponse response = new AuthResponse(
                token,
                user.getName(),
                user.getEmail(),
                roles,
                "Login successful"
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<MessageResponse> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponse("Authorization header with Bearer token is required"));
        }

        String token = authHeader.substring(7);
        tokenBlacklistService.revoke(token, jwtService.extractExpirationInstant(token));

        return ResponseEntity.ok(new MessageResponse("Logout successful"));
    }
}
