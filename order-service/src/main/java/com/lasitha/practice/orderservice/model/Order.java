package com.lasitha.practice.orderservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
@Data
@Entity
@Builder
@Table(name = "t_orders")
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String orderNumber;
    @OneToMany(cascade = CascadeType.ALL)
    private List<OrderItems> orderItemList;
}
