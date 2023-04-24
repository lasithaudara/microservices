package com.lasitha.practice.productservice.controller;

import com.lasitha.practice.productservice.model.ProductRequest;
import com.lasitha.practice.productservice.model.ProductResponse;
import com.lasitha.practice.productservice.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/product")
public record ProductController(ProductService productService) {

    @GetMapping
    @ResponseStatus(HttpStatus.CREATED)
    public List<ProductResponse> getAllProduct(){
        return productService.getAllProducts();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createProduct(@RequestBody ProductRequest productReq){
        productService.createProduct(productReq);
    }
}
