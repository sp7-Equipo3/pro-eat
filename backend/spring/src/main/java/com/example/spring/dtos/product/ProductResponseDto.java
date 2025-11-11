package com.example.spring.dtos.product;

import lombok.Data;

@Data
public class ProductResponseDto {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private String category;
}
