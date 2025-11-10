package com.example.spring.exceptions.custom;

/**
 * Excepción lanzada cuando un recurso solicitado no se encuentra en la base de datos.
 * <p>
 * Esta excepción resulta en una respuesta HTTP 404 NOT FOUND.
 * </p>
 * <p>
 * Ejemplo de uso:
 * <pre>
 * throw new ResourceNotFoundException("Producto", "id", 123);
 * // Mensaje: "Producto no encontrado con id: 123"
 * </pre>
 * </p>
 */
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Constructor que genera un mensaje formateado automáticamente.
     *
     * @param resourceName Nombre del recurso (ej: "Producto", "Usuario")
     * @param fieldName Nombre del campo usado para buscar (ej: "id", "username")
     * @param fieldValue Valor del campo que se buscó
     */
    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s no encontrado con %s: %s", resourceName, fieldName, fieldValue));
    }

    /**
     * Constructor con mensaje personalizado.
     *
     * @param message Mensaje de error personalizado
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}

