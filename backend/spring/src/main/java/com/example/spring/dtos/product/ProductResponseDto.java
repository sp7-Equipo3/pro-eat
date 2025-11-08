package com.example.spring.dtos.product;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ProductResponseDto {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private String category;
}
