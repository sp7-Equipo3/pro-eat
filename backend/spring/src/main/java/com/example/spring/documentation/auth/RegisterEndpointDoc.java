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
 * Swagger documentation for POST /api/auth/register.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(
        summary = "Registrar nuevo usuario",
        description = "Crea una nueva cuenta de usuario en el sistema"
)
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "201",
                description = "Usuario registrado exitosamente",
                content = @Content(
                        mediaType = "application/json",
                        examples = @ExampleObject(value = """
                {
                  "success": true,
                  "message": "Usuario registrado exitosamente.",
                  "data": {
                    "username": "john_doe",
                    "role": "USER"
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
                  "message": "Error de validación en los siguientes campos: username, password",
                  "error": "VALIDATION_ERROR",
                  "timestamp": "2025-11-09T14:30:00",
                  "path": "/api/auth/register",
                  "details": {
                    "username": "el nombre de usuario debe tener entre 3 y 20 caracteres",
                    "password": "la contraseña es obligatoria"
                  }
                }
            """)
                )
        ),
        @ApiResponse(
                responseCode = "409",
                description = "Conflicto - Usuario ya existe",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponse.class),
                        examples = @ExampleObject(value = """
                {
                  "success": false,
                  "message": "Usuario ya existe con username: john_doe",
                  "error": "DUPLICATE_RESOURCE",
                  "timestamp": "2025-11-09T14:30:00",
                  "path": "/api/auth/register"
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
                  "message": "Error interno del servidor. Contacta al administrador.",
                  "error": "INTERNAL_SERVER_ERROR",
                  "timestamp": "2025-11-09T14:30:00",
                  "path": "/api/auth/register"
                }
            """)
                )
        )
})
public @interface RegisterEndpointDoc {}

