package com.example.inventory_and_order_management.dto;

import java.math.BigDecimal;

public record OrderItemResponse(
        Long productId,
        String productName,
        Integer quantity,
        BigDecimal priceAtPurchase
) {}