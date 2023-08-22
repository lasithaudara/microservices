package com.lasitha.practice.notification.consumer.event;

import lombok.Builder;

@Builder
public record OrderPlacedEvent(String orderNumber) {
}
