package com.rishi.AuthanticationApplication.dtos;

import com.rishi.AuthanticationApplication.model.Role;
import com.rishi.AuthanticationApplication.other.Provider;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

    private UUID id;
    private String name;

    private String email;
    private String password;
    private boolean enable = true;
    private Instant createdAt = Instant.now();
    private Instant updatedAt = Instant.now();

    private Provider provider = Provider.LOCAL;

    private Set<RoleDto> roles = new HashSet<>();
}
