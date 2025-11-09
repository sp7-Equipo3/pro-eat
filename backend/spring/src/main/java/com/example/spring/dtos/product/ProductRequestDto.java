package com.example.spring.dtos.product;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Schema(description = "DTO para crear o actualizar productos")
public class ProductRequestDto {

    @Schema(description = "Nombre del producto", example = "Queso Cheddar")
    @NotBlank(message = "el nombre del producto es obligatorio")
    @Size(min = 3, max = 100, message = "el nombre debe tener entre 3 y 100 caracteres")
    private String name;

    @Schema(description = "Descripción del producto", example = "Queso cheddar madurado de calidad premium")
    @Size(max = 500, message = "la descripción no puede superar los 500 caracteres")
    private String description;

    @Schema(description = "Precio del producto", example = "250.50")
    @NotNull(message = "el precio es obligatorio")
    @Positive(message = "el precio debe ser mayor a 0")
    @DecimalMin(value = "0.01", message = "el precio mínimo es 0.01")
    @DecimalMax(value = "999999.99", message = "el precio máximo es 999999.99")
    private Double price;

    @Schema(description = "Categoría del producto", example = "Lácteos")
    @NotBlank(message = "la categoría es obligatoria")
    @Size(min = 3, max = 50, message = "la categoría debe tener entre 3 y 50 caracteres")
    private String category;
}
