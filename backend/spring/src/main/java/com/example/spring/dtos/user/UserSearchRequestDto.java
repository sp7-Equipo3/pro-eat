package com.example.spring.dtos.user;

import com.example.spring.enums.Role;

public record UserSearchRequestDto(
        String username,
        Role role
) {
}
