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
@Tag(name = "03 - Productos",
        description = "Endpoints para gestión de productos")
public class ProductController {
    private final ProductService productService;

    @GetAllProductEndpointDoc
    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResult<?>> getAllProducts(@ParameterObject Pageable pageable) {
        Page<ProductResponseDto> products = productService.getAllProducts(pageable);
        return ResponseEntity.ok(ApiResult.success(products, "Productos obtenidos exitosamente."));
    }

    @GetProductEndpointDoc
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResult<?>> getProductById(@PathVariable Long id) {
        ProductResponseDto product = productService.getProductById(id);
        return ResponseEntity.ok(ApiResult.success(product, "Producto encontrado exitosamente."));
    }

    @CreateProductEndpointDoc
    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResult<?>> createProduct(@RequestBody @Valid ProductRequestDto requestDto) {
        ProductResponseDto createdProduct = productService.createProduct(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResult.success(createdProduct,
                "Producto creada correctamente."));
    }

    @UpdateProductEndpointDoc
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResult<?>> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductRequestDto requestDto) {
        ProductResponseDto updatedProduct = productService.editProduct(id, requestDto);
        return ResponseEntity.ok(ApiResult.success(updatedProduct, "Producto actualizado exitosamente."));
    }

    @DeleteProductEndpointDoc
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResult<?>> deleteProduct(@PathVariable Long id) {
        productService.deleteProductById(id);
        return ResponseEntity.ok(ApiResult.success("Producto eliminado exitosamente."));
    }

    @GetMapping("/filter")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ApiResult<?>> filterProducts(
        @RequestParam(required = false) String name,
        @RequestParam(required = false) String category,
        @RequestParam(required = false) double minPrice,
        @RequestParam(required = false) double maxPrice,
        @ParameterObject Pageable pageable       ) 
        {
            Page<ProductResponseDto> result = productService.filterProducts(name,category,minPrice,maxPrice,pageable);
            return ResponseEntity.ok(ApiResult.success(result,"Producto filtrado con exito"));
        }
    

}
