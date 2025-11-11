package com.example.spring.dtos.user;

import java.util.UUID;

public record UserSearchResponseDto(
        UUID id,
        String username,
        String role
) {
}
