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
 * Swagger documentation for PUT /api/products/id.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(
        summary = "Actualizar Producto",
        description = """
        Actualiza la información de un producto existente identificado por su ID. \s
        Accesible solo para usuarios autorizados con rol <strong>ADMIN o USER</strong>
        """,
        security = @SecurityRequirement(name = "bearer-key")
)
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "Producto actualizado correctamente",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(example = """
                        {
                          "success": true,
                          "message": "Producto actualizado correctamente.",
                          "data": {
                            "id": 1,
                            "name": "Queso",
                            "description": "...",
                            "price": "200",
                            "category": "Almacén"
                          }
                        }
                        """)
                )
        ),
        @ApiResponse(
                responseCode = "400",
                description = "Solicitud inválida: error de validación o datos incorrectos",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(example = """
                        {
                          "statusCode": 400,
                          "errorCode": "VALIDATION_ERROR",
                          "message": "Error de validación",
                          "details": "name: no puede estar vacío",
                          "timestamp": "2025-11-10T20:12:00Z",
                          "path": "/api/products/{id}"
                        }
                        """)
                )
        ),
        @ApiResponse(
                responseCode = "401",
                description = "No autorizado (token ausente o inválido)",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(example = """
                        {
                          "statusCode": 401,
                          "errorCode": "AUTH_ERROR",
                          "message": "Acceso no autorizado",
                          "details": "...",
                          "timestamp": "2025-11-10T20:12:00Z",
                          "path": "/api/products/{id}"
                        }
                        """)
                )
        ),
        @ApiResponse(
                responseCode = "403",
                description = "Acceso denegado por falta de permisos",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(example = """
                        {
                          "statusCode": 403,
                          "errorCode": "FORBIDDEN",
                          "message": "Acceso denegado",
                          "details": "...",
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
                          "message": "Producto no encontrado con id: 12",
                          "details": "...",
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
public @interface UpdateProductEndpointDoc {}