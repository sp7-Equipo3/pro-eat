package com.example.spring.exceptions.custom;

/**
 * Excepción para indicar violaciones de reglas de negocio.
 * <p>
 * Esta excepción resulta en una respuesta HTTP 400 BAD REQUEST.
 * </p>
 * <p>
 * Usar cuando se violan reglas de negocio o lógica de la aplicación,
 * como intentar eliminar un recurso que tiene dependencias, operaciones
 * no permitidas según el estado actual, etc.
 * </p>
 * <p>
 * Ejemplo de uso:
 * <pre>
 * throw new BusinessException("No se puede eliminar un producto con órdenes activas");
 * </pre>
 * </p>
 */
public class BusinessException extends RuntimeException {

    /**
     * Constructor con mensaje descriptivo de la regla de negocio violada.
     *
     * @param message Mensaje descriptivo del error de negocio
     */
    public BusinessException(String message) {
        super(message);
    }

    /**
     * Constructor con mensaje y causa original.
     *
     * @param message Mensaje descriptivo del error de negocio
     * @param cause Excepción original que causó este error
     */
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}

