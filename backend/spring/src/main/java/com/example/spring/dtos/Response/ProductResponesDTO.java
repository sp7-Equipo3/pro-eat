package com.example.spring.dtos.Response;

import lombok.*;

@Data
public class ProductResponesDTO {
    private Long id;
    private String name;
    private String description;
    private double price;
    private String category;

}
