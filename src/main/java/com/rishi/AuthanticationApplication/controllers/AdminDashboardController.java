package com.rishi.AuthanticationApplication.controllers;

import com.rishi.AuthanticationApplication.dtos.AdminDashboardResponse;
import com.rishi.AuthanticationApplication.dtos.UserSummaryDto;
import com.rishi.AuthanticationApplication.model.User;
import com.rishi.AuthanticationApplication.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminDashboardController {

    private final UserRepository userRepository;

    @GetMapping("/dashboard")
    public ResponseEntity<AdminDashboardResponse> dashboard() {
        List<User> users = userRepository.findAll();

        List<UserSummaryDto> summaries = users.stream()
                .map(u -> new UserSummaryDto(
                        u.getId(),
                        u.getName(),
                        u.getEmail(),
                        u.getRoles().stream().map(r -> r.getName()).collect(Collectors.toSet())
                ))
                .toList();

        return ResponseEntity.ok(new AdminDashboardResponse(users.size(), summaries));
    }
}

