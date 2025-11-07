package com.example.spring.dtos.Request;

import lombok.*;


@Data
public class ProductRequestDTO {

    private String name;
    private String description;
    private double price;
    private String category;
    private Integer calories;
    private boolean available;


}
