package com.lasitha.practice.orderservice.model;

import lombok.Builder;

import java.util.List;
@Builder
public record OrderRequest(List<OrderItemDto> orderItemList) {
}
