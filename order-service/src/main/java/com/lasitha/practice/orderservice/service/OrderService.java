package com.lasitha.practice.orderservice.service;

import com.lasitha.practice.orderservice.model.*;
import com.lasitha.practice.orderservice.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public record OrderService(OrderRepository orderRepository, WebClient.Builder webClientBuilder) {

    /**
     * @param orderRequest - pojo from request
     * @apiNote - service for placing an order
     */
    public void placeOrder(OrderRequest orderRequest){
        List<OrderItems> orderItemList = orderRequest.orderItemList().stream()
                .map(OrderService::mapToEntity)
                .toList();

        List<String> skuCodeList = orderItemList.parallelStream()
                .map(OrderItems::getSkuCode)
                .toList();

        InventoryResponse[] responseList = webClientBuilder.build().get()
                .uri("http://inventory-service/api/v1/inventory",
                        uriBuilder -> uriBuilder.queryParam("skuCode", skuCodeList).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();

        boolean allItemsIsInStock = Arrays.stream(Objects.requireNonNull(responseList))
                .allMatch(InventoryResponse::isInStock);

        if (allItemsIsInStock) {
            Order order = Order.builder()
                    .orderNumber(UUID.randomUUID().toString())
                    .orderItemList(orderItemList)
                    .build();

            orderRepository.save(order);
        }else
            throw new IllegalStateException("order items are not available in stock");
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
