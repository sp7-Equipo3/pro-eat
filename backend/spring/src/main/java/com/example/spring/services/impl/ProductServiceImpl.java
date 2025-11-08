package com.example.spring.services.impl;

import com.example.spring.dtos.product.ProductRequestDto;
import com.example.spring.dtos.product.ProductResponseDto;
import com.example.spring.mappers.ProductMapper;
import com.example.spring.models.Product;
import com.example.spring.repositories.ProductRepository;
import com.example.spring.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    ProductMapper mapper;

    @Override
    public Page<ProductResponseDto> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(mapper::toResponseDto);
    }

    @Override
    public ProductResponseDto getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado con ID: " + id));
        return mapper.toResponseDto(product);
    }

    @Override
    public ProductResponseDto createProduct(ProductRequestDto requestDto) {
        Product order = mapper.toEntity(requestDto);
        return mapper.toResponseDto(productRepository.save(order));
    }

    @Override
    public void deleteProductById(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado con ID: " + id);
        }
        productRepository.deleteById(id);
    }

    @Override
    public ProductResponseDto editProduct(Long id, ProductRequestDto dto) {
        Product auxProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado con ID: " + id));

        if (dto.getName() != null)            auxProduct.setName(dto.getName());
        if (dto.getDescription() != null)   auxProduct.setDescription(dto.getDescription());
        if (dto.getPrice() != null)          auxProduct.setPrice(dto.getPrice());
        if (dto.getCategory() != null)       auxProduct.setCategory(dto.getCategory());

        return mapper.toResponseDto(productRepository.save(auxProduct));
    }
}
