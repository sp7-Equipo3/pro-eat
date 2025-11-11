package com.example.spring.dtos.user;

import com.example.spring.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserUpdateRequestDto (
    @Schema(description = "Nombre de usuario único", example = "john_doe")
    @NotBlank(message = "el nombre de usuario es obligatorio")
    @Size(min = 3, max = 20, message = "el nombre de usuario debe tener entre 3 y 20 caracteres")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "el nombre de usuario solo puede contener letras, números y guiones bajos")
    String username,

    @Schema(description = "Rol del usuario. Valores permitidos: ADMIN, USER", example = "ADMIN", allowableValues = {"ADMIN", "USER"})
    @NotNull(message = "El rol del usuario es requerido")
    Role role
){}
