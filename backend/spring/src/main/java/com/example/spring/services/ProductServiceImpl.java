package com.example.spring.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.spring.dtos.Request.ProductRequestDTO;
import com.example.spring.dtos.Response.ProductResponesDTO;
import com.example.spring.entity.ProductEntity;
import com.example.spring.repositories.ProductRepository;
import com.example.spring.services.interfaces.ProductService;

@Service
public class ProductServiceImpl implements ProductService{

    private final ProductRepository repository;

    public ProductServiceImpl(ProductRepository repository){
        this.repository=repository;
    }

    @Override
    public ProductResponesDTO createProduct(ProductRequestDTO dto) {
        ProductEntity product = new ProductEntity();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setAvailable(dto.isAvailable());
        product.setCalories(dto.getCalories());
        product.setPrice(dto.getPrice());
        product.setCategory(dto.getCategory());
        repository.save(product);

        ProductResponesDTO productResponesDTO = new ProductResponesDTO();

        productResponesDTO.setId(product.getId());
        productResponesDTO.setName(product.getName());
        productResponesDTO.setCategory(product.getCategory());
        productResponesDTO.setDescription(product.getDescription());
        productResponesDTO.setPrice(product.getPrice());

        return productResponesDTO;

    }

    @Override
    public List<ProductResponesDTO> listProducts() {
         List<ProductEntity> products = repository.findAll();
         List<ProductResponesDTO> response = new ArrayList<>();

         for(ProductEntity p:products){
            ProductResponesDTO dto = new ProductResponesDTO();
            dto.setId(p.getId());
            dto.setCategory(p.getCategory());
            dto.setDescription(p.getDescription());
            dto.setName(p.getName());
            dto.setPrice(p.getPrice());
            response.add(dto);
         }

        return response;
    }

    @Override
    public ProductResponesDTO getProduct(Long id) {
        List<ProductResponesDTO> product = listProducts();

        for(ProductResponesDTO pr:product){
            if(pr.getId().equals(id)){
                return pr;
            }
        }
        return null;
    }

    @Override
    public void deleteProduct(Long id) {
        repository.deleteById(id);
    }
    
}
