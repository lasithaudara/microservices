package com.lasitha.practice.orderservice.model;

import lombok.Builder;

@Builder
public record InventoryResponse(String skuCode, Boolean isInStock) {
}
