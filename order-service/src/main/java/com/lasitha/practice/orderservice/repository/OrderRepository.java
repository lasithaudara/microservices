package com.lasitha.practice.orderservice.repository;

import com.lasitha.practice.orderservice.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
