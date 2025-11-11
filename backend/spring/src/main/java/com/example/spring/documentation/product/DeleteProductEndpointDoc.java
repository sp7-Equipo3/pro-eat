package com.example.spring.documentation.product;

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
                responseCode = "204",
                description = "Producto eliminado correctamente",
                content = @Content // sin body (no content)
        ),
        @ApiResponse(
                responseCode = "401",
                description = "No autorizado (token ausente o inválido)",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(example = """
                        {
                          "statusCode": 401,
                          "errorCode": "UNAUTHORIZED",
                          "message": "Acceso no autorizado",
                          "details": "Token inválido o expirado",
                          "timestamp": "2025-11-10T20:12:00Z",
                          "path": "/api/products/{id}"
                        }
                        """)
                )
        ),
        @ApiResponse(
                responseCode = "403",
                description = "Acceso denegado: el rol del usuario no tiene permisos para eliminar",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(example = """
                        {
                          "statusCode": 403,
                          "errorCode": "FORBIDDEN",
                          "message": "Acceso denegado",
                          "details": "Se requiere rol ADMIN",
                          "timestamp": "2025-11-10T20:12:00Z",
                          "path": "/api/products/{id}"
                        }
                        """)
                )
        ),
        @ApiResponse(
                responseCode = "404",
                description = "Producto no encontrado",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(example = """
                        {
                          "statusCode": 404,
                          "errorCode": "NOT_FOUND",
                          "message": "Producto no encontrado con id: {id}",
                          "details": "No existe un producto con el ID proporcionado",
                          "timestamp": "2025-11-10T20:12:00Z",
                          "path": "/api/products/{id}"
                        }
                        """)
                )
        ),
        @ApiResponse(
                responseCode = "500",
                description = "Error interno del servidor",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(example = """
                        {
                          "statusCode": 500,
                          "errorCode": "INTERNAL_SERVER_ERROR",
                          "message": "Error inesperado",
                          "details": "NullPointerException ...",
                          "timestamp": "2025-11-10T20:12:00Z",
                          "path": "/api/products/{id}"
                        }
                        """)
                )
        )
})

public @interface DeleteProductEndpointDoc {}