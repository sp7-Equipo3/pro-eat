package com.example.spring.documentation.auth;

import com.example.spring.exceptions.dto.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.*;

/**
 * Swagger documentation for POST /api/auth/login.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(
        summary = "Iniciar sesión",
        description = "Autentica un usuario y devuelve un token JWT"
)
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "Login exitoso",
                content = @Content(
                        mediaType = "application/json",
                        examples = @ExampleObject(value = """
                {
                  "success": true,
                  "message": "Login exitoso.",
                  "data": {
                    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
                  }
                }
            """)
                )
        ),
        @ApiResponse(
                responseCode = "400",
                description = "Error de validación - Datos incorrectos o incompletos",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponse.class),
                        examples = @ExampleObject(value = """
                {
                  "success": false,
                  "message": "Error de validación en los siguientes campos: username",
                  "error": "VALIDATION_ERROR",
                  "timestamp": "2025-11-09T14:30:00",
                  "path": "/api/auth/login",
                  "details": {
                    "username": "el nombre de usuario es obligatorio"
                  }
                }
            """)
                )
        ),
        @ApiResponse(
                responseCode = "401",
                description = "Credenciales incorrectas",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponse.class),
                        examples = @ExampleObject(value = """
                {
                  "success": false,
                  "message": "Credenciales incorrectas. Verifica tu usuario y contraseña.",
                  "error": "AUTHENTICATION_ERROR",
                  "timestamp": "2025-11-09T14:30:00",
                  "path": "/api/auth/login"
                }
            """)
                )
        ),
        @ApiResponse(
                responseCode = "500",
                description = "Error interno del servidor",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponse.class),
                        examples = @ExampleObject(value = """
                {
                  "success": false,
                  "message": "Error interno del servidor.",
                  "error": "INTERNAL_SERVER_ERROR",
                  "timestamp": "2025-11-09T14:30:00",
                  "path": "/api/auth/login"
                }
            """)
                )
        )
})
public @interface LoginEndpointDoc {}

