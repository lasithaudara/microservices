package com.lasitha.practice.inventoryservice.controller;

import com.lasitha.practice.inventoryservice.model.InventoryResponse;
import com.lasitha.practice.inventoryservice.service.InventoryService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/inventory")
public record InventoryController(InventoryService inventoryService) {

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryResponse> inStock(@RequestParam List<String> skuCode){
        return inventoryService.inStock(skuCode);
    }
}
