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
 * Swagger documentation for GET /api/products.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(
        summary = "Obtener todos los productos",
        description = """
        Retorna una lista completa de productos registradas en el sistema. \s
        Accesible para usuarios con roles: <strong>ADMIN o USER</strong>
        """,
        security = @SecurityRequirement(name = "bearer-key")
)
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "Lista paginada de productos obtenidos exitosamente",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(example = """
                {
                  "content": [
                    {
                       "id": 1,
                       "name": "Queso",
                       "description": "...",
                       "price": "200",
                       "category": "Almacén"
                    },
                    {
                       "id": 2,
                       "name": "Pan",
                       "description": "...",
                       "price": "...",
                       "category": "..."
                    }
                  ],
                  "pageable": {
                    "sort": {
                      "sorted": false,
                      "unsorted": true,
                      "empty": true
                    },
                    "pageNumber": 0,
                    "pageSize": 10,
                    "offset": 0,
                    "paged": true,
                    "unpaged": false
                  },
                  "totalElements": 2,
                  "totalPages": 1,
                  "last": true,
                  "first": true,
                  "sort": {
                    "sorted": false,
                    "unsorted": true,
                    "empty": true
                  },
                  "numberOfElements": 2,
                  "size": 10,
                  "number": 0,
                  "empty": false
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
                  "errorCode": "UNAUTHORIZED",
                  "message": "Acceso no autorizado",
                  "details": "...",
                  "timestamp": "2025-11-10T20:12:00Z",
                  "path": "/api/products"
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
                  "path": "/api/products"
                }
            """)
                )
        ),
        @ApiResponse(
                responseCode = "404",
                description = "Recurso no encontrado (aplica si no hay productos registrados)",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(example = """
                {
                  "statusCode": 404,
                  "errorCode": "NOT_FOUND",
                  "message": "No se encontraron productos registrados.",
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
                        schema = @Schema(example = """
                {
                  "statusCode": 500,
                  "errorCode": "INTERNAL_SERVER_ERROR",
                  "message": "Error inesperado",
                  "details": "...",
                  "timestamp": "2025-11-10T20:12:00Z",
                  "path": "/api/products"
                }
            """)
                )
        )
})
public @interface GetAllProductEndpointDoc {}