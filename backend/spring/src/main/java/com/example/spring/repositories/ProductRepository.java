package com.example.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.spring.entity.ProductEntity;

public interface ProductRepository extends JpaRepository<ProductEntity,Long> {
    
    
}
