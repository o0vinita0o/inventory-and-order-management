package com.example.inventory_and_order_management.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
        Long orderId,
        LocalDateTime orderDate,
        String status,
        BigDecimal totalAmount,
        List<OrderItemResponse> items
) {


}