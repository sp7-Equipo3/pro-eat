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
                        examples = @ExampleObject(value = """
                {
                  "success": true,
                  "message": "Producto creada correctamente.",
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
                responseCode = "400",
                description = "Error de validación - Datos incorrectos o incompletos",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponse.class),
                        examples = @ExampleObject(value = """
                {
                  "success": false,
                  "message": "Error de validación: {name=no puede estar vacío, price=debe ser mayor que 0}",
                  "error": "VALIDATION_ERROR",
                  "timestamp": "2025-11-09T14:30:00",
                  "path": "/api/products"
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
public @interface CreateProductEndpointDoc {}