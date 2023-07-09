package com.lasitha.practice.inventoryservice.service;

import com.lasitha.practice.inventoryservice.model.Inventory;
import com.lasitha.practice.inventoryservice.model.InventoryResponse;
import com.lasitha.practice.inventoryservice.repository.InventoryRepository;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
@Slf4j
@Service
@AllArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    @SneakyThrows /* @SneakyThrows - not recommended for the production env */
    public List<InventoryResponse> inStock(List<String> skuCode){
        return inventoryRepository.findBySkuCodeIn(skuCode)
                .stream()
                    .map(this::mapToResponse).toList();
    }

    private InventoryResponse mapToResponse(Inventory inventory){
        return InventoryResponse.builder()
                .skuCode(inventory.getSkuCode())
                .isInStock(inventory.getQuantity() > 0)
                .build();
    }
}
