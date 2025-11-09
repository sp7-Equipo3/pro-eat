package com.example.spring.exceptions.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Record que representa la respuesta estándar para errores de la API.
 * Proporciona un formato consistente para todas las respuestas de error.
 */
@Schema(description = "Respuesta estándar para errores de la API")
public record ErrorResponse(

        @Schema(description = "Indica si la operación fue exitosa", example = "false")
        boolean success,

        @Schema(description = "Mensaje de error legible para el usuario",
                example = "Producto no encontrado con ID: 123")
        String message,

        @Schema(description = "Tipo de error técnico",
                example = "RESOURCE_NOT_FOUND")
        String error,

        @Schema(description = "Fecha y hora en que ocurrió el error",
                example = "2025-11-09T14:30:00")
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime timestamp,

        @Schema(description = "Ruta del endpoint donde ocurrió el error",
                example = "/api/products/123")
        String path,

        @Schema(description = "Detalles específicos del error por campo (solo para errores de validación)",
                example = """
                {
                  "name": "el nombre es obligatorio",
                  "price": "el precio debe ser mayor a 0"
                }
                """)
        @JsonInclude(JsonInclude.Include.NON_NULL)
        Map<String, String> details
) {
    /**
     * Constructor simplificado que establece success=false y timestamp=now automáticamente.
     *
     * @param message Mensaje descriptivo del error
     * @param error Tipo de error (nombre de la excepción)
     * @param path Ruta del endpoint donde ocurrió el error
     */
    public ErrorResponse(String message, String error, String path) {
        this(false, message, error, LocalDateTime.now(), path, null);
    }

    /**
     * Constructor con details para errores de validación.
     *
     * @param message Mensaje descriptivo del error
     * @param error Tipo de error (nombre de la excepción)
     * @param path Ruta del endpoint donde ocurrió el error
     * @param details Mapa de errores por campo
     */
    public ErrorResponse(String message, String error, String path, Map<String, String> details) {
        this(false, message, error, LocalDateTime.now(), path, details);
    }
}
