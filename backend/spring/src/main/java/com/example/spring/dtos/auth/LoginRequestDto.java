package com.example.spring.dtos.auth;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDto(
        @Schema(description = "Nombre de usuario.", example = "admin")
        @NotBlank(message = "Username is required")
        String username,

        @Schema(description = "Contraseña del usuario.", example = "admin")
        @NotBlank(message = "Password is required")
        String password
) {
}
