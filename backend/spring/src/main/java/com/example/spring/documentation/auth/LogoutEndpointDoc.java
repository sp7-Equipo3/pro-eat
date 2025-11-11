package com.example.spring.documentation.auth;

import com.example.spring.exceptions.dto.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.lang.annotation.*;

/**
 * Swagger documentation for POST /api/auth/logout.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(
        summary = "Cerrar sesión",
        description = """
        Invalida el token JWT actual del usuario para cerrar sesión de forma segura. \s
        Requiere autenticación mediante token JWT válido.
        """,
        security = @SecurityRequirement(name = "bearer-key")
)
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "Logout exitoso",
                content = @Content(
                        mediaType = "application/json",
                        examples = @ExampleObject(value = """
                {
                  "success": true,
                  "message": "Logout exitoso.",
                  "data": null
                }
            """)
                )
        ),
        @ApiResponse(
                responseCode = "401",
                description = "No autenticado - Token ausente o inválido",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponse.class),
                        examples = @ExampleObject(value = """
                {
                  "success": false,
                  "message": "Acceso denegado. Debes iniciar sesión para acceder a este recurso.",
                  "error": "AUTHENTICATION_REQUIRED",
                  "timestamp": "2025-11-09T14:30:00",
                  "path": "/api/auth/logout"
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
                  "path": "/api/auth/logout"
                }
            """)
                )
        )
})
public @interface LogoutEndpointDoc {}

