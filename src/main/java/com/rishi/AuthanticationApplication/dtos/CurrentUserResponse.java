package com.rishi.AuthanticationApplication.dtos;

import java.util.Set;

public record CurrentUserResponse(
        String name,
        String email,
        Set<String> roles
) {
}

