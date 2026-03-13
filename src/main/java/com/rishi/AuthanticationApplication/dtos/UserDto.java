package com.rishi.AuthanticationApplication.dtos;

import com.rishi.AuthanticationApplication.other.Provider;
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

    @Builder.Default
    private boolean enable = true;

    @Builder.Default
    private Instant createdAt = Instant.now();

    @Builder.Default
    private Instant updatedAt = Instant.now();

    @Builder.Default
    private Provider provider = Provider.LOCAL;

    @Builder.Default
    private Set<RoleDto> roles = new HashSet<>();
}
