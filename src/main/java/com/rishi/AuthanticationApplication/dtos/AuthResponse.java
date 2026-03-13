package com.rishi.AuthanticationApplication.dtos;

import java.util.Set;

public record AuthResponse(
        String token,
        String name,
        String email,
        Set<String> roles,
        String message
) {
}

