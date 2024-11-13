package com.moysklad.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moysklad.dto.SaleDto;
import com.moysklad.service.SaleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SaleController.class)
class SaleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SaleService saleService;

    @InjectMocks
    private SaleController saleController;

    @Autowired
    private ObjectMapper objectMapper;

    private SaleDto saleDto;

    @BeforeEach
    public void setup() {
        saleDto = new SaleDto(Long.valueOf("1"), "Поставка 001", Long.valueOf("1"), 10, 150.0);
    }

    @Test
    void testCreateSale() throws Exception {
        when(saleService.createSale(any(SaleDto.class))).thenReturn(saleDto);

        mockMvc.perform(post("/api/sales")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(saleDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.productId").value(saleDto.getProductId()))
                .andExpect(jsonPath("$.quantity").value(saleDto.getQuantity()))
                .andExpect(jsonPath("$.price").value(saleDto.getPrice()));
    }

    @Test
    void testGetSaleById() throws Exception {
        when(saleService.getSaleById(1L)).thenReturn(saleDto);

        mockMvc.perform(get("/api/sales/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(saleDto.getProductId()))
                .andExpect(jsonPath("$.quantity").value(saleDto.getQuantity()))
                .andExpect(jsonPath("$.price").value(saleDto.getPrice()));
    }

    @Test
    void testUpdateSale() throws Exception {
        when(saleService.updateSale(anyLong(), any(SaleDto.class))).thenReturn(saleDto);

        mockMvc.perform(put("/api/sales/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(saleDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(saleDto.getProductId()))
                .andExpect(jsonPath("$.quantity").value(saleDto.getQuantity()))
                .andExpect(jsonPath("$.price").value(saleDto.getPrice()));
    }

    @Test
    void testDeleteSale() throws Exception {
        when(saleService.deleteSale(Long.valueOf("1"))).thenReturn(true);

        mockMvc.perform(delete("/api/sales/1"))
                .andExpect(status().isNoContent());
    }
}
