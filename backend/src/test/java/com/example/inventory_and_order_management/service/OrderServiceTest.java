package com.example.inventory_and_order_management.service;

import com.example.inventory_and_order_management.dto.OrderRequest;
import com.example.inventory_and_order_management.dto.OrderResponse;
import com.example.inventory_and_order_management.entity.Order;
import com.example.inventory_and_order_management.entity.Product;
import com.example.inventory_and_order_management.repository.OrderRepository;
import com.example.inventory_and_order_management.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    private Product mockProduct;

    @BeforeEach
    void setUp() {
        mockProduct = new Product();
        mockProduct.setId(1L);
        mockProduct.setName("Mechanical Keyboard");
        mockProduct.setPrice(new BigDecimal("120.00"));
        mockProduct.setStockQuantity(50);
    }

    @Test
    void createOrder_Success_DecrementsStockAndReturnsResponse() {
        OrderRequest request = new OrderRequest(1L, 2);

        // Stub findByIdWithLock instead of findById
        when(productRepository.findByIdWithLock(1L)).thenReturn(Optional.of(mockProduct));

        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order orderToSave = invocation.getArgument(0);
            orderToSave.setId(100L);
            return orderToSave;
        });

        OrderResponse response = orderService.createOrder(request);

        assertNotNull(response);
        assertEquals("COMPLETE", response.status());
        assertEquals(new BigDecimal("240.00"), response.totalAmount());
        assertEquals(48, mockProduct.getStockQuantity());

        verify(productRepository, times(1)).save(mockProduct);
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void createOrder_Fails_WhenInsufficientStock() {
        mockProduct.setStockQuantity(1); // Only 1 in stock, requested 2
        OrderRequest request = new OrderRequest(1L, 2);

        // Stub findByIdWithLock instead of findById
        when(productRepository.findByIdWithLock(1L)).thenReturn(Optional.of(mockProduct));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            orderService.createOrder(request);
        });

        // Matches the exact message in OrderService.java
        assertEquals("Insufficient stock.", exception.getMessage());

        verify(productRepository, never()).save(any(Product.class));
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void createOrder_Fails_WhenProductNotFound() {
        OrderRequest request = new OrderRequest(99L, 2);

        // Stub findByIdWithLock instead of findById
        when(productRepository.findByIdWithLock(99L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            orderService.createOrder(request);
        });

        assertEquals("Product not found", exception.getMessage());
        verify(orderRepository, never()).save(any(Order.class));
    }
}