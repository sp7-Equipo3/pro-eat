package com.example.spring.exceptions;

import com.example.spring.exceptions.custom.BusinessException;
import com.example.spring.exceptions.custom.DuplicateResourceException;
import com.example.spring.exceptions.custom.ResourceNotFoundException;
import com.example.spring.exceptions.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Manejador global de excepciones para toda la aplicación.
 * Captura y procesa excepciones lanzadas por los controladores,
 * retornando respuestas JSON consistentes.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Método helper para crear respuestas de error consistentes.
     */
    private ResponseEntity<ErrorResponse> buildErrorResponse(
            String message, String errorCode, HttpStatus status, HttpServletRequest request) {
        ErrorResponse error = new ErrorResponse(
                false,
                message,
                errorCode,
                LocalDateTime.now(),
                request.getRequestURI()
        );
        return ResponseEntity.status(status).body(error);
    }

    // ==================== Excepciones Personalizadas ====================

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
            ResourceNotFoundException ex, HttpServletRequest request) {
        log.error("Recurso no encontrado en {}: {}", request.getRequestURI(), ex.getMessage());
        return buildErrorResponse(ex.getMessage(), "RESOURCE_NOT_FOUND", HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(
            BusinessException ex, HttpServletRequest request) {
        log.error("Error de negocio en {}: {}", request.getRequestURI(), ex.getMessage());
        return buildErrorResponse(ex.getMessage(), "BUSINESS_ERROR", HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateResourceException(
            DuplicateResourceException ex, HttpServletRequest request) {
        log.error("Recurso duplicado en {}: {}", request.getRequestURI(), ex.getMessage());
        return buildErrorResponse(ex.getMessage(), "DUPLICATE_RESOURCE", HttpStatus.CONFLICT, request);
    }

    // ==================== Validación ====================

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        log.error("Error de validación en {}: {}", request.getRequestURI(), ex.getMessage());

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            errors.put(fieldName, error.getDefaultMessage());
        });

        return buildErrorResponse(
                "Error de validación: " + errors,
                "VALIDATION_ERROR",
                HttpStatus.BAD_REQUEST,
                request
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
            IllegalArgumentException ex, HttpServletRequest request) {
        log.error("Argumento ilegal en {}: {}", request.getRequestURI(), ex.getMessage());
        return buildErrorResponse(ex.getMessage(), "INVALID_ARGUMENT", HttpStatus.BAD_REQUEST, request);
    }

    // ==================== Autenticación ====================

    @ExceptionHandler({
            BadCredentialsException.class,
            InternalAuthenticationServiceException.class,
            AuthenticationException.class
    })
    public ResponseEntity<ErrorResponse> handleAuthenticationException(
            Exception ex, HttpServletRequest request) {
        log.error("Error de autenticación en {}: {}", request.getRequestURI(), ex.getMessage());

        String message = ex instanceof BadCredentialsException
                ? "Credenciales incorrectas. Verifica tu usuario y contraseña."
                : "Error de autenticación. Verifica tus credenciales.";

        return buildErrorResponse(message, "AUTHENTICATION_ERROR", HttpStatus.UNAUTHORIZED, request);
    }

    // ==================== Rutas y Métodos HTTP ====================

    @ExceptionHandler({NoHandlerFoundException.class, NoResourceFoundException.class})
    public ResponseEntity<ErrorResponse> handleNotFoundException(HttpServletRequest request) {
        log.error("Ruta no encontrada: {}", request.getRequestURI());
        return buildErrorResponse(
                String.format("La ruta '%s' no existe.", request.getRequestURI()),
                "ROUTE_NOT_FOUND",
                HttpStatus.NOT_FOUND,
                request
        );
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotSupportedException(
            HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        log.error("Método HTTP no soportado en {}: {}", request.getRequestURI(), ex.getMethod());

        String supportedMethods = ex.getSupportedHttpMethods() != null
                ? ex.getSupportedHttpMethods().toString()
                : "ninguno";

        return buildErrorResponse(
                String.format("Método '%s' no permitido. Métodos soportados: %s", ex.getMethod(), supportedMethods),
                "METHOD_NOT_ALLOWED",
                HttpStatus.METHOD_NOT_ALLOWED,
                request
        );
    }

    // ==================== Catch-All ====================

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex, HttpServletRequest request) {
        log.error("Error no manejado en {}: {}", request.getRequestURI(), ex.getMessage(), ex);
        return buildErrorResponse(
                "Error interno del servidor.",
                "INTERNAL_SERVER_ERROR",
                HttpStatus.INTERNAL_SERVER_ERROR,
                request
        );
    }
}

