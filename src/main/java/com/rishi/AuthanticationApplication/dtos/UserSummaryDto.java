package com.rishi.AuthanticationApplication.dtos;

import java.util.Set;
import java.util.UUID;

public record UserSummaryDto(
        UUID id,
        String name,
        String email,
        Set<String> roles
) {
}

