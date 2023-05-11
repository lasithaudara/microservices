package com.lasitha.practice.orderservice.service;

import com.lasitha.practice.orderservice.model.Order;
import com.lasitha.practice.orderservice.model.OrderItems;
import com.lasitha.practice.orderservice.model.OrderItemDto;
import com.lasitha.practice.orderservice.model.OrderRequest;
import com.lasitha.practice.orderservice.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public record OrderService(OrderRepository orderRepository) {

    /**
     * @param orderRequest - pojo from request
     * @apiNote - service for placing an order
     */
    public void placeOrder(OrderRequest orderRequest){
        List<OrderItems> orderItemList = orderRequest.orderItemList().stream()
                .map(OrderService::mapToEntity)
                .toList();

        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        order.setOrderItemList(orderItemList);

        orderRepository.save(order);
    }



    /**
     * @param orderItemDto - pojo from request
     * @return OrderItem from OrderItemDto
     */
    private static OrderItems mapToEntity(OrderItemDto orderItemDto){

                OrderItems orderItem = new OrderItems();
        orderItem.setPrice(orderItemDto.price());
        orderItem.setSkuCode(orderItemDto.skuCode());
        orderItem.setQuantity(orderItemDto.quantity());

        return orderItem;
    }
}
