package com.lasitha.practice.orderservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
@Data
@Entity
@Table(name = "t_order_items")
@AllArgsConstructor
@NoArgsConstructor
public class OrderItems {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String skuCode;
    private BigDecimal price;
    private Integer quantity;

}
