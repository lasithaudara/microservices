package com.lasitha.practice.inventoryservice.model;

import lombok.Builder;

@Builder
public record InventoryResponse(String skuCode, Boolean isInStock) {
}
