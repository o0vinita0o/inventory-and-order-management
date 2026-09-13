package com.example.inventory_and_order_management.repository;

import com.example.inventory_and_order_management.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}