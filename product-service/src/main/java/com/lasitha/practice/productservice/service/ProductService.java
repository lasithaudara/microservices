package com.lasitha.practice.productservice.service;

import com.lasitha.practice.productservice.model.Product;
import com.lasitha.practice.productservice.model.ProductRequest;
import com.lasitha.practice.productservice.model.ProductResponse;
import com.lasitha.practice.productservice.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public record ProductService(ProductRepository productRepository) {
    public void createProduct(ProductRequest productReq){
        Product product = Product.builder()
                .name(productReq.name())
                .description(productReq.description())
                .price(productReq.price())
                .build();
        productRepository.save(product);
        log.info("Product is created - {}", productReq.toString());
    }

    public List<ProductResponse> getAllProducts(){
        return productRepository.findAll().stream().map(ProductService::mapToDto).toList();
    }

    private static ProductResponse mapToDto(Product product){
       return ProductResponse.builder().id(product.getId())
               .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build(); }
}
