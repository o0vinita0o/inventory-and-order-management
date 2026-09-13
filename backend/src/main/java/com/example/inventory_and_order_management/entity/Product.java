package com.example.inventory_and_order_management.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Product implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private BigDecimal price;
    private Integer stockQuantity;
}