package com.example.spring.exceptions.custom;

/**
 * Excepción lanzada cuando se intenta crear un recurso que ya existe.
 * <p>
 * Esta excepción resulta en una respuesta HTTP 409 CONFLICT.
 * </p>
 * <p>
 * Usar cuando se detectan conflictos de unicidad, como intentar registrar
 * un usuario con un username ya existente, crear un producto con un código duplicado, etc.
 * </p>
 * <p>
 * Ejemplo de uso:
 * <pre>
 * throw new DuplicateResourceException("Usuario", "username", "john_doe");
 * // Mensaje: "Usuario ya existe con username: john_doe"
 * </pre>
 * </p>
 */
public class DuplicateResourceException extends RuntimeException {

    /**
     * Constructor que genera un mensaje formateado automáticamente.
     *
     * @param resourceName Nombre del recurso (ej: "Usuario", "Producto")
     * @param fieldName Nombre del campo que está duplicado (ej: "username", "email")
     * @param fieldValue Valor del campo que está duplicado
     */
    public DuplicateResourceException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s ya existe con %s: %s", resourceName, fieldName, fieldValue));
    }

    /**
     * Constructor con mensaje personalizado.
     *
     * @param message Mensaje de error personalizado
     */
    public DuplicateResourceException(String message) {
        super(message);
    }
}

