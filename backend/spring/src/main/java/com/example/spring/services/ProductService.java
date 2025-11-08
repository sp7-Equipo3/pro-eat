package com.example.spring.services;

import com.example.spring.dtos.product.ProductRequestDto;
import com.example.spring.dtos.product.ProductResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    Page<ProductResponseDto> getAllProducts(Pageable pageable);
    ProductResponseDto getProductById(Long id);
    ProductResponseDto createProduct(ProductRequestDto dto);
    void deleteProductById(Long id);
    ProductResponseDto editProduct(Long id, ProductRequestDto ProductRequestDto);
}
