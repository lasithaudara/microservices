package com.lasitha.practice.productservice.controller;

import com.lasitha.practice.productservice.service.InventoryService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/inventory")
public record InventoryController(InventoryService inventoryService) {

    @GetMapping("{skuCode}")
    @ResponseStatus(HttpStatus.OK)
    public boolean inStock(@PathVariable String skuCode){
        return inventoryService.inStock(skuCode);
    }
}
