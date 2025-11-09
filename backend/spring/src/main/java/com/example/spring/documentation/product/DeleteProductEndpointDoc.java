package com.example.spring.documentation.product;

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
 * Swagger documentation for DELETE /api/products/id.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(
        summary = "Eliminar producto",
        description = """
        Elimina definitivamente un producto por su ID único. \s
        Requiere autenticación de usuarios con rol <strong>ADMIN</strong>
        """,
        security = @SecurityRequirement(name = "bearer-key")
)
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "Producto eliminado correctamente",
                content = @Content(
                        mediaType = "application/json",
                        examples = @ExampleObject(value = """
                {
                  "success": true,
                  "message": "Producto eliminado exitosamente.",
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
                  "path": "/api/products/1"
                }
                """)
                )
        ),
        @ApiResponse(
                responseCode = "403",
                description = "Sin permisos - Usuario autenticado pero sin rol necesario",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponse.class),
                        examples = @ExampleObject(value = """
                {
                  "success": false,
                  "message": "No tienes permisos suficientes para acceder a este recurso.",
                  "error": "ACCESS_DENIED",
                  "timestamp": "2025-11-09T14:30:00",
                  "path": "/api/products/1"
                }
                """)
                )
        ),
        @ApiResponse(
                responseCode = "404",
                description = "Producto no encontrado",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponse.class),
                        examples = @ExampleObject(value = """
                {
                  "success": false,
                  "message": "Producto no encontrado con id: 1",
                  "error": "RESOURCE_NOT_FOUND",
                  "timestamp": "2025-11-09T14:30:00",
                  "path": "/api/products/1"
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
                  "path": "/api/products/1"
                }
                """)
                )
        )
})

public @interface DeleteProductEndpointDoc {}