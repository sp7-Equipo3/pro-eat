package com.example.spring.services.interfaces;

import java.util.List;

import com.example.spring.dtos.Request.ProductRequestDTO;
import com.example.spring.dtos.Response.ProductResponesDTO;

public interface ProductService {
    ProductResponesDTO createProduct(ProductRequestDTO dto);
    List<ProductResponesDTO> listProducts();
    ProductResponesDTO getProduct(Long id);
    void deleteProduct(Long id);
    
}
