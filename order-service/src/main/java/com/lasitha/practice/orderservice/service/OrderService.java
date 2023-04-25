package com.lasitha.practice.orderservice.service;

import com.lasitha.practice.orderservice.model.Order;
import com.lasitha.practice.orderservice.model.OrderItem;
import com.lasitha.practice.orderservice.model.OrderItemDto;
import com.lasitha.practice.orderservice.model.OrderRequest;
import com.lasitha.practice.orderservice.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public record OrderService(OrderRepository orderRepository) {

    /**
     * @param orderRequest - pojo from request
     * @apiNote - service for placing an order
     */
    public void placeOrder(OrderRequest orderRequest){
        List<OrderItem> orderItemList = orderRequest.orderItemList().stream()
                .map(OrderService::mapToEntity)
                .toList();

        Order order = Order.builder()
                .orderNumber(UUID.randomUUID().toString())
                .orderItemList(orderItemList)
                .build();

        orderRepository.save(order);
    }



    /**
     * @param orderItemDto - pojo from request
     * @return OrderItem from OrderItemDto
     */
    private static OrderItem mapToEntity(OrderItemDto orderItemDto){
        return OrderItem.builder()
                .price(orderItemDto.price())
                .skuCode(orderItemDto.skuCode())
                .quantity(orderItemDto.quantity())
                .build();
    }
}