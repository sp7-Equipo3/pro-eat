package com.example.spring.exceptions.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

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
                example = "ResourceNotFoundException")
        String error,

        @Schema(description = "Fecha y hora en que ocurrió el error",
                example = "2025-11-09T14:30:00")
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime timestamp,

        @Schema(description = "Ruta del endpoint donde ocurrió el error",
                example = "/api/products/123")
        String path
) {
    /**
     * Constructor simplificado que establece success=false y timestamp=now automáticamente.
     *
     * @param message Mensaje descriptivo del error
     * @param error Tipo de error (nombre de la excepción)
     * @param path Ruta del endpoint donde ocurrió el error
     */
    public ErrorResponse(String message, String error, String path) {
        this(false, message, error, LocalDateTime.now(), path);
    }
}

