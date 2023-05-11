package com.lasitha.practice.productservice.service;

import com.lasitha.practice.productservice.repository.InventoryRepository;
import org.springframework.stereotype.Service;

@Service
public record InventoryService(InventoryRepository inventoryRepository) {
    public boolean inStock(String skuCode){
        return inventoryRepository.findBySkuCode(skuCode).isPresent();
    }
}
