package com.example.spring.mappers;

import com.example.spring.dtos.product.ProductRequestDto;
import com.example.spring.dtos.product.ProductResponseDto;
import com.example.spring.enums.Role;
import com.example.spring.models.Product;
import org.mapstruct.*;

@Mapper(componentModel = "spring", imports = {Role.class})
public interface ProductMapper {

    @Mapping(target="id", ignore = true)
    @Mapping(target="name", source = "name")
    @Mapping(target="description", source = "description")
    @Mapping(target="category", source = "category")
    Product toEntity(ProductRequestDto requestDTO);

    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "category", source = "category")
    ProductResponseDto toResponseDto(Product product);
}
