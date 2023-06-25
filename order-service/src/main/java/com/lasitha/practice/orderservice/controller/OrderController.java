package com.lasitha.practice.orderservice.controller;

import com.lasitha.practice.orderservice.model.OrderRequest;
import com.lasitha.practice.orderservice.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/order")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CircuitBreaker(name = "inventory", fallbackMethod = "fallbackMethod")
    public String placeOrder(@RequestBody OrderRequest orderRequest){
        return orderService.placeOrder(orderRequest);
    }

    @ResponseStatus(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS)
    public String fallbackMethod(OrderRequest orderRequest, RuntimeException runtimeException){
        log.error(runtimeException.getMessage());
        return "Oops! something went wrong!";
    }

    @GetMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String placeOrder(){
        return "Hit method";
    }
}


