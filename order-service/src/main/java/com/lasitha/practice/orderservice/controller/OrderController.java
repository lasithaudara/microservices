package com.lasitha.practice.orderservice.controller;

import com.lasitha.practice.orderservice.model.OrderRequest;
import com.lasitha.practice.orderservice.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/order")
public record OrderController(OrderService orderService) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void placeOrder(@RequestBody OrderRequest orderRequest){
        orderService.placeOrder(orderRequest);
    }
}
