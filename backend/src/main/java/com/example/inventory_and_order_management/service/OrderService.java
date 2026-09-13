package com.example.inventory_and_order_management.service;

import com.example.inventory_and_order_management.dto.OrderRequest;
import com.example.inventory_and_order_management.dto.OrderResponse;
import com.example.inventory_and_order_management.dto.OrderItemResponse;
import com.example.inventory_and_order_management.entity.Order;
import com.example.inventory_and_order_management.entity.OrderItem;
import com.example.inventory_and_order_management.entity.Product;
import com.example.inventory_and_order_management.repository.OrderRepository;
import com.example.inventory_and_order_management.repository.ProductRepository;
import jakarta.validation.Valid;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    @CacheEvict(value = "products", allEntries = true)
    public OrderResponse createOrder(@Valid OrderRequest request) {
        Product product = productRepository.findByIdWithLock(request.productId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        if (product.getStockQuantity() < request.quantity()) {
            throw new IllegalArgumentException("Insufficient stock.");
        }

        product.setStockQuantity(product.getStockQuantity() - request.quantity());
        productRepository.save(product);

        Order order = new Order();
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("COMPLETE");

        OrderItem item = new OrderItem();
        item.setProduct(product);
        item.setQuantity(request.quantity());
        item.setPriceAtPurchase(product.getPrice());
        order.addOrderItem(item);

        BigDecimal totalAmount = product.getPrice().multiply(BigDecimal.valueOf(request.quantity()));
        order.setTotalAmount(totalAmount);

        Order savedOrder = orderRepository.save(order);
        return mapToResponse(savedOrder);
    }

    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll(Sort.by(Sort.Direction.DESC, "id"))
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public OrderResponse mapToResponse(Order order) {
        List<OrderItemResponse> itemResponses = order.getItems().stream()
                .map(item -> new OrderItemResponse(
                        item.getProduct().getId(),
                        item.getProduct().getName(),
                        item.getQuantity(),
                        item.getPriceAtPurchase()
                ))
                .toList();

        return new OrderResponse(
                order.getId(),
                order.getOrderDate(),
                order.getStatus(),
                order.getTotalAmount(),
                itemResponses
        );
    }
}