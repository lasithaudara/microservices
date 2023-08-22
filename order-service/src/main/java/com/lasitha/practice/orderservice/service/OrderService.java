package com.lasitha.practice.orderservice.service;

import com.lasitha.practice.orderservice.event.OrderPlacedEvent;
import com.lasitha.practice.orderservice.model.*;
import com.lasitha.practice.orderservice.repository.OrderRepository;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;
    private final Tracer tracer;
    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

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

        Span inventoryServiceLookup = tracer.nextSpan().name("InventoryServiceLookup");

        try( Tracer.SpanInScope isSpanInScope = tracer.withSpan(inventoryServiceLookup.start())){

            inventoryServiceLookup.tag("call", "inventory-service");

            InventoryResponse[] inventoryResponseArray = webClientBuilder
                    .build().get()
                    .uri("http://inventory-service/api/v1/inventory", uriBuilder ->
                            uriBuilder.queryParam("skuCode", skuCodes).build())
                    .retrieve()
                    .bodyToMono(InventoryResponse[].class).block();

            assert inventoryResponseArray != null;
            boolean productsInStock = Arrays.stream(inventoryResponseArray)
                    .allMatch(InventoryResponse::isInStock);
            if (productsInStock) {
                Order order = Order.builder()
                        .orderNumber(UUID.randomUUID().toString())
                        .orderItemList(orderItemList)
                        .build();
                orderRepository.save(order);

                kafkaTemplate.send("notificationTopic", OrderPlacedEvent.builder()
                        .orderNumber(order.getOrderNumber()).build());
                return "Order is successful";
            } else {
                throw new IllegalArgumentException("Product is not in stock, please try again later");
            }
        }finally {
            inventoryServiceLookup.end();
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
