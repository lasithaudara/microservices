package com.lasitha.practice.orderservice.model;

import lombok.Builder;

import java.util.List;

@Builder
public record OrderResponse(Long id, String orderName, List<OrderItems> orderItemList) {
}
