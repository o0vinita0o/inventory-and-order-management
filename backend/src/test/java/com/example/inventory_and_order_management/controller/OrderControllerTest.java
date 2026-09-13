package com.example.inventory_and_order_management.controller;

import com.example.inventory_and_order_management.dto.OrderRequest;
import com.example.inventory_and_order_management.dto.OrderResponse;
import com.example.inventory_and_order_management.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void placeOrder_ReturnsCreatedStatusAndOrderResponse() throws Exception {
        // Aligned with the constructor used in OrderServiceTest
        OrderRequest request = new OrderRequest(1L, 2);

        // Assuming OrderResponse has an all-args constructor. Adjust parameters as needed based on your record/class definition.
        OrderResponse response = new OrderResponse(
                100L,
                LocalDateTime.now(),
                "COMPLETE",
                new BigDecimal("240.00"),
                List.of()
        );

        when(orderService.createOrder(any(OrderRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("COMPLETE"))
                .andExpect(jsonPath("$.totalAmount").value(240.00));
    }

    @Test
    void getAllOrders_ReturnsOkStatusAndOrderList() throws Exception {
        OrderResponse response = new OrderResponse(
                100L,
                LocalDateTime.now(),
                "COMPLETE",
                new BigDecimal("240.00"),
                List.of()
        );

        when(orderService.getAllOrders()).thenReturn(List.of(response));

        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].status").value("COMPLETE"));
    }
}