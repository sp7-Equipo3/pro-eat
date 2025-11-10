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
 * Swagger documentation for GET /api/products.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(
        summary = "Obtener producto por ID",
        description = """
        Devuelve los detalles de un producto específico mediante su ID. \s
        Accesible para usuarios con roles: <strong>ADMIN o USER</strong>
        """,
        security = @SecurityRequirement(name = "bearer-key")
)
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "Producto encontrado exitosamente",
                content = @Content(
                        mediaType = "application/json",
                        examples = @ExampleObject(value = """
                {
                  "success": true,
                  "message": "Producto encontrado exitosamente.",
                  "data": {
                    "id": 1,
                    "name": "Queso",
                    "description": "Queso fresco de calidad premium",
                    "price": 200.50,
                    "category": "Almacén"
                  }
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
                  "message": "Producto no encontrado con id: 123",
                  "error": "RESOURCE_NOT_FOUND",
                  "timestamp": "2025-11-09T14:30:00",
                  "path": "/api/products/123"
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
                  "path": "/api/products/123"
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
                  "path": "/api/products/123"
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
                  "path": "/api/products/123"
                }
            """)
                )
        )
})
public @interface GetProductEndpointDoc {}