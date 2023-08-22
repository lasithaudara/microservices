package com.lasitha.practice.orderservice.event;

import lombok.Builder;

@Builder
public record OrderPlacedEvent(String orderNumber) {
}
