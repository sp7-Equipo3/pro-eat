package com.example.spring.controllers;

import com.example.spring.documentation.product.*;
import com.example.spring.dtos.product.ProductRequestDto;
import com.example.spring.dtos.product.ProductResponseDto;
import com.example.spring.services.ProductService;
import com.example.spring.utils.ApiResult;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
@Tag(name = "02 - Productos",
        description = "Endpoints para gestión de productos")
public class ProductController {
    private final ProductService productService;

    @GetAllProductEndpointDoc
    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Page<ProductResponseDto>> getAllProducts(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok(productService.getAllProducts(pageable));
    }

    @GetProductEndpointDoc
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @CreateProductEndpointDoc
    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResult<?>> createProduct(@RequestBody ProductRequestDto requestDto) {
        ProductResponseDto createdProduct = productService.createProduct(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResult.success(createdProduct,
                "Producto creada correctamente."));
    }

    @UpdateProductEndpointDoc
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ProductResponseDto> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductRequestDto requestDto) {
        ProductResponseDto updatedOrder = productService.editProduct(id, requestDto);
        return ResponseEntity.ok(updatedOrder);
    }

    @DeleteProductEndpointDoc
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProductById(id);
        return ResponseEntity.noContent().build();
    }

}
