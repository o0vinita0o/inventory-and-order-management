package com.example.inventory_and_order_management.controller;

import com.example.inventory_and_order_management.entity.Product;
import com.example.inventory_and_order_management.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.data.domain.Sort;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductRepository productRepository;

    @Test
    void getAllProducts_ReturnsOkStatusAndSortedProductList() throws Exception {
        // Aligned with the entity setters used in OrderServiceTest
        Product product = new Product();
        product.setId(1L);
        product.setName("Mechanical Keyboard");
        product.setPrice(new BigDecimal("120.00"));
        product.setStockQuantity(50);

        when(productRepository.findAll(any(Sort.class))).thenReturn(List.of(product));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].name").value("Mechanical Keyboard"))
                .andExpect(jsonPath("$[0].price").value(120.00))
                .andExpect(jsonPath("$[0].stockQuantity").value(50));
    }
}