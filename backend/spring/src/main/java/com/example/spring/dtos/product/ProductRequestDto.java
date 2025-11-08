package com.example.spring.dtos.product;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ProductRequestDto{
    @NotNull
    private String name;
    private String description;
    private Double price;
    private String category;
}
