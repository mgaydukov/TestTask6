package com.moysklad.controller;

import com.moysklad.dto.ProductDto;
import com.moysklad.exception.ProductNotFoundException;
import com.moysklad.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private ProductDto productDto;

    @BeforeEach
    public void setup() {
        productDto = new ProductDto(Long.valueOf("1"), "Sample Product", "Description", 10.0, true);
    }

    @Test
    public void testGetAllProductsWithoutParams() throws Exception {
        when(productService.getAllProducts(null, null, null, null, null, "asc", 10))
                .thenReturn(Collections.singletonList(productDto));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].name").value("Sample Product"));
    }

    @Test
    public void testGetProductById() throws Exception {
        when(productService.getProductById(Long.valueOf("1"))).thenReturn(productDto);

        mockMvc.perform(get("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Sample Product"));
    }

    @Test
    public void testGetProductById_NotFound() throws Exception {
        when(productService.getProductById(Long.valueOf("2"))).thenThrow(new ProductNotFoundException("Product with ID 2 not found"));

        mockMvc.perform(get("/api/products/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Product with ID 2 not found"));
    }

    @Test
    public void testCreateProduct() throws Exception {
        when(productService.createProduct(any(ProductDto.class))).thenReturn(productDto);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Sample Product\", \"description\": \"Description\", \"price\": 10.0, \"inStock\": true}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Sample Product"));
    }

    @Test
    public void testUpdateProduct() throws Exception {
        when(productService.updateProduct(eq(Long.valueOf("1")), any(ProductDto.class))).thenReturn(productDto);

        mockMvc.perform(put("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Updated Product\", \"description\": \"New Description\", \"price\": 20.0, \"inStock\": false}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Sample Product"));
    }

    @Test
    public void testDeleteProduct() throws Exception {
        when(productService.deleteProduct(Long.valueOf("1"))).thenReturn(true);

        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteProduct_NotFound() throws Exception {
        when(productService.deleteProduct(Long.valueOf("2"))).thenThrow(new ProductNotFoundException("Product with ID 2 not found"));;

        mockMvc.perform(delete("/api/products/2"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Product with ID 2 not found"));
    }

    @Test
    public void testCreateProductWithLongName() throws Exception {
        String longName = "A".repeat(256);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"" + longName + "\", \"description\": \"Sample\", \"price\": 10.0, \"inStock\": true}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").value("Product name cannot be longer than 255 characters"));
    }
}