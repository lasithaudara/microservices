package com.lasitha.practice.orderservice.service;

import com.lasitha.practice.orderservice.model.*;
import com.lasitha.practice.orderservice.repository.OrderRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;

    /**
     * @param orderRequest - pojo from request
     * @apiNote - service for placing an order
     */
    public String placeOrder(OrderRequest orderRequest) {

        List<OrderItems> orderItemList = orderRequest.orderItemList().stream()
                .map(OrderService::mapToEntity)
                .toList();

        List<String> skuCodes = orderItemList.stream()
                .map(OrderItems::getSkuCode)
                .toList();

        Mono<InventoryResponse[]> responseMono = webClientBuilder
                .build().get()
                .uri("http://inventory-service/api/v1/inventory", uriBuilder ->
                        uriBuilder.queryParam("skuCode", skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class);

        AtomicBoolean allProductsInStock = new AtomicBoolean(false); // Initialized with a default value

        responseMono.subscribe(
                inventoryResponseArray -> {
                    boolean productsInStock = Arrays.stream(inventoryResponseArray)
                            .allMatch(InventoryResponse::isInStock);
                    allProductsInStock.set(productsInStock);
                },
                Throwable::printStackTrace
        );
        boolean result = allProductsInStock.get();
        if (result) {
            Order order = Order.builder()
                    .orderNumber(UUID.randomUUID().toString())
                    .orderItemList(orderItemList)
                    .build();
            orderRepository.save(order);
            return "order placed successfully";
        } else {
            throw new IllegalArgumentException("Product is not in stock, please try again later");
        }
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
