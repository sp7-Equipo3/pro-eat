package com.example.spring.dtos.auth;

import java.util.UUID;

public record JwtDataDto(
        UUID id,
        String username,
        String role
) {
}
