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
                        examples = @ExampleObject(value = """
                {
                  "success": true,
                  "message": "Productos obtenidos exitosamente.",
                  "data": {
                    "content": [
                      {
                         "id": 1,
                         "name": "Queso",
                         "description": "Queso fresco de calidad premium",
                         "price": 200.50,
                         "category": "Almacén"
                      },
                      {
                         "id": 2,
                         "name": "Pan",
                         "description": "Pan integral artesanal",
                         "price": 150.75,
                         "category": "Panadería"
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
                  "path": "/api/products"
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
                        schema = @Schema(implementation = ErrorResponse.class),
                        examples = @ExampleObject(value = """
                {
                  "success": false,
                  "message": "Error interno del servidor. Contacta al administrador.",
                  "error": "INTERNAL_SERVER_ERROR",
                  "timestamp": "2025-11-09T14:30:00",
                  "path": "/api/products"
                }
            """)
                )
        )
})
public @interface GetAllProductEndpointDoc {}