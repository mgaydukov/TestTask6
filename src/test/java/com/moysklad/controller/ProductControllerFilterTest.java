package com.moysklad.controller;

import com.moysklad.dto.ProductDto;
import com.moysklad.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.*;

@WebMvcTest(ProductController.class)
public class ProductControllerFilterTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    private List<ProductDto> products;

    @BeforeEach
    public void setup() {
        ProductDto phone1 = new ProductDto(1L, "phone", "Описание товара 1", 100.0, true);
        ProductDto phone2 = new ProductDto(3L, "phone", "Описание товара 3", 40.0, true);
        ProductDto phone3 = new ProductDto(5L, "phone", "Описание товара 5", 55.0, false);
        ProductDto book1 = new ProductDto(2L, "book", "Описание товара 2", 10.0, true);
        ProductDto book2 = new ProductDto(6L, "book", "Описание товара 6", 70.0, true);
        ProductDto toy = new ProductDto(7L, "toy", "Описание товара 7", 11.55, true);
        products = Arrays.asList(phone1, phone2, phone3, book1, book2,  toy);
    }

    @Test
    public void testFilterByName() throws Exception {
        List<ProductDto> filteredProducts = Arrays.asList(products.get(3), products.get(4)); // book1, book2
        when(productService.getAllProducts("book", null, null, null, null, "asc", 10)).thenReturn(filteredProducts);

        mockMvc.perform(get("/api/products")
                        .param("name", "book")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(2)))
                .andExpect(jsonPath("$[0].name", is("book")))
                .andExpect(jsonPath("$[1].id", is(6)))
                .andExpect(jsonPath("$[1].name", is("book")));
    }

    @Test
    public void testFilterByNameContainsOo() throws Exception {
        List<ProductDto> filteredProducts = Arrays.asList(products.get(3), products.get(4)); // toy
        when(productService.getAllProducts("oo", null, null, null, null, "asc", 10)).thenReturn(filteredProducts);

        mockMvc.perform(get("/api/products")
                        .param("name", "oo")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(2)))
                .andExpect(jsonPath("$[0].name", is("book")))
                .andExpect(jsonPath("$[1].id", is(6)))
                .andExpect(jsonPath("$[1].name", is("book")));
    }

    @Test
    public void testFilterByPriceRange() throws Exception {
        List<ProductDto> filteredProducts = Arrays.asList(products.get(3), products.get(5)); // book1, toy
        when(productService.getAllProducts(null, 10.0, 15.0, null, null, "asc", 10)).thenReturn(filteredProducts);

        mockMvc.perform(get("/api/products")
                        .param("minPrice", "10.0")
                        .param("maxPrice", "15.0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(2))) // book1
                .andExpect(jsonPath("$[1].id", is(7))); // toy
    }

    @Test
    public void testFilterByInStock() throws Exception {
        List<ProductDto> allInStockProducts = Arrays.asList(products.get(0), products.get(1), products.get(3), products.get(4), products.get(5)); // Все кроме phone3
        when(productService.getAllProducts(null, null, null, true, null, "asc", 10)).thenReturn(allInStockProducts);

        mockMvc.perform(get("/api/products")
                        .param("inStock", "true")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(jsonPath("$[0].inStock", is(true)));
    }

    @Test
    public void testFilterByNameAndMinPrice() throws Exception {
        List<ProductDto> filteredProducts = Arrays.asList(products.get(4)); // book2
        when(productService.getAllProducts("book", 50.0, null, null, null, "asc", 10)).thenReturn(filteredProducts);

        mockMvc.perform(get("/api/products")
                        .param("name", "book")
                        .param("minPrice", "50.0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(6))); // book2
    }

    @Test
    public void testFilterByNameAndMaxPrice() throws Exception {
        List<ProductDto> filteredProducts = Arrays.asList(products.get(3)); // book1
        when(productService.getAllProducts("book", null, 20.0, null, null, "asc", 10)).thenReturn(filteredProducts);

        mockMvc.perform(get("/api/products")
                        .param("name", "book")
                        .param("maxPrice", "20.0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(2))); // book1
    }


    @Test
    public void testFilterByNameContainsOoAndSortByPriceDesc() throws Exception {
        List<ProductDto> filteredProducts = Arrays.asList(products.get(4), products.get(3)); // book2, book1
        when(productService.getAllProducts("oo", null, null, null, "price", "desc", 10)).thenReturn(filteredProducts);

        mockMvc.perform(get("/api/products")
                        .param("name", "oo")
                        .param("sortBy", "price")
                        .param("sortDirection", "desc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(6))) // book2
                .andExpect(jsonPath("$[1].id", is(2))); // book1
    }

    @Test
    public void testFilterByPriceRangeAndInStockAndSortByPriceDesc() throws Exception {
        List<ProductDto> filteredProducts = Arrays.asList(products.get(4), products.get(1), products.get(5));

        when(productService.getAllProducts(null, 11.0, 80.0, true, "price", "desc", 10)).thenReturn(filteredProducts);

        mockMvc.perform(get("/api/products")
                        .param("minPrice", "11")
                        .param("maxPrice", "80")
                        .param("inStock", "true")
                        .param("sortBy", "price")
                        .param("sortDirection", "desc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id", is(6))) // book2
                .andExpect(jsonPath("$[1].id", is(3))) // phone2
                .andExpect(jsonPath("$[2].id", is(7))); // toy
    }
}
