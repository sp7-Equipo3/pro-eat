package com.example.spring.dtos.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "DTO para registro de nuevos usuarios")
public record RegisterRequestDto(

        @Schema(description = "Nombre de usuario único", example = "john_doe")
        @NotBlank(message = "el nombre de usuario es obligatorio")
        @Size(min = 3, max = 20, message = "el nombre de usuario debe tener entre 3 y 20 caracteres")
        @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "el nombre de usuario solo puede contener letras, números y guiones bajos")
        String username,

        @Schema(description = "Contraseña del usuario", example = "Password123@")
        @NotBlank(message = "la contraseña es obligatoria")
        @Size(min = 8, max = 64, message = "La contraseña debe tener entre 8 y 64 caracteres")
        @Pattern(regexp = "^(?=.*[A-ZÑ])(?=.*[a-zñ])(?=.*\\d)(?=.*[-@#$%^&*.,()_+{}|;:'\"<>/!¡¿?])[A-ZÑa-zñ\\d-@#$%^&*.,()_+{}|;:'\"<>/!¡¿?]{6,}$",
                message = "La contraseña debe contener al menos una letra mayuscula, una letra minuscula, un numero, y un caracter especial.")
        String password
) {
}
