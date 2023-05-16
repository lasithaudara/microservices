package com.lasitha.practice.inventoryservice.service;

import com.lasitha.practice.inventoryservice.model.Inventory;
import com.lasitha.practice.inventoryservice.model.InventoryResponse;
import com.lasitha.practice.inventoryservice.repository.InventoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public record InventoryService(InventoryRepository inventoryRepository) {
    public List<InventoryResponse> inStock(List<String> skuCode){
        return inventoryRepository.findBySkuCodeIn(skuCode)
                .stream()
                    .map(this::mapToResponce).toList();
    }

    private InventoryResponse mapToResponce(Inventory inventory){
        return InventoryResponse.builder()
                .skuCode(inventory.getSkuCode())
                .isInStock(inventory.getQuantity() > 0)
                .build();
    }
}
