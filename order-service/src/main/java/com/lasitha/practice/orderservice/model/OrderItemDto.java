package com.lasitha.practice.orderservice.model;

import lombok.Builder;

import java.math.BigDecimal;

public record OrderItemDto(String skuCode, BigDecimal price, Integer quantity) {
}
