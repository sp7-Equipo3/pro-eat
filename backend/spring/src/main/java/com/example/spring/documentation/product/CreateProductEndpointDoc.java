
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
 * Swagger documentation for POST /api/products.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(
        summary = "Crear nuevo producto",
        description = """
        <strong>Reglas/validaciones relevantes:</strong>
        <ul>
          <li><code>name</code> es obligatorio y debe existir.</li>
        </ul>
        Crea un nuevo producto en el sistema especificando nombre, descripcion, precio y categoria.\s
        Requiere autenticación de usuarios con rol <strong>ADMIN o User</strong>
        """,
        security = @SecurityRequirement(name = "bearer-key")
)
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "201",
                description = "Producto creado correctamente",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(
                                example = """
                    {
                      "success": true,
                      "message": "Producto creado correctamente.",
                      "data": {
                        "id": 1,
                        "name": "Queso",
                        "description": "...",
                        "price": "200",
                        "category": "Almacén",
                      }
                    }
                """
                        )
                )
        ),
        @ApiResponse(
                responseCode = "400",
                description = "Solicitud inválida: error de validación o datos incorrectos",
                content = @Content(
                        mediaType = "application/json",
                        examples = {
                                @ExampleObject(
                                        name = "Campos requeridos faltantes",
                                        summary = "Cuando falta el nombre o otro campo obligatorio",
                                        value = """
                                            {
                                              "statusCode": 400,
                                              "errorCode": "VALIDATION_ERROR",
                                              "message": "Error validation with data",
                                              "details": "name: no puede estar vacío",
                                              "timestamp": "2025-11-10T20:12:00Z",
                                              "path": "/api/products"
                                            }
                                        """
                                )
                        }
                )
        ),
        @ApiResponse(
                responseCode = "401",
                description = "No autorizado (token ausente o inválido)",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(
                                example = """
                    {
                      "statusCode": 401,
                      "errorCode": "UNAUTHORIZED",
                      "message": "Acceso no autorizado",
                      "details": "...",
                      "timestamp": "2025-11-10T20:12:00Z",
                      "path": "/api/products"
                    }
                """
                        )
                )
        ),
        @ApiResponse(
                responseCode = "403",
                description = "Acceso denegado por falta de permisos",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(
                                example = """
                    {
                      "statusCode": 403,
                      "errorCode": "FORBIDDEN",
                      "message": "Acceso denegado",
                      "details": "Se requiere rol ADMIN o USER",
                      "timestamp": "2025-11-10T20:12:00Z",
                      "path": "/api/products"
                    }
                """
                        )
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
                          "details": "...",
                          "timestamp": "2025-11-10T20:12:00Z",
                          "path": "/api/products"
                        }
                        """)
                )
        ),
        @ApiResponse(
                responseCode = "500",
                description = "Error interno del servidor",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(
                                example = """
                    {
                      "statusCode": 500,
                      "errorCode": "INTERNAL_ERROR",
                      "message": "Internal Error Server",
                      "details": "NullPointerException...",
                      "timestamp": "2025-11-10T20:12:00Z",
                      "path": "/api/products"
                    }
                """
                        )
                )
        )
})
public @interface CreateProductEndpointDoc {}
