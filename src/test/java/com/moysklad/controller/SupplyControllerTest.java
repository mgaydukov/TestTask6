package com.moysklad.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moysklad.dto.SupplyDto;
import com.moysklad.service.SupplyService;
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

@WebMvcTest(SupplyController.class)
class SupplyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SupplyService supplyService;

    @InjectMocks
    private SupplyController supplyController;

    @Autowired
    private ObjectMapper objectMapper;

    private SupplyDto supplyDto;

    @BeforeEach
    public void setup() {
        supplyDto = new SupplyDto(Long.valueOf("1"), "Продажа 001", Long.valueOf("1"), 10);
    }

    @Test
    void testCreateSupply() throws Exception {
        when(supplyService.createSupply(any(SupplyDto.class))).thenReturn(supplyDto);

        mockMvc.perform(post("/api/supplies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(supplyDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.productId").value(supplyDto.getProductId()))
                .andExpect(jsonPath("$.quantity").value(supplyDto.getQuantity()));
    }

    @Test
    void testGetSupplyById() throws Exception {
        when(supplyService.getSupplyById(1L)).thenReturn(supplyDto);

        mockMvc.perform(get("/api/supplies/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(supplyDto.getProductId()))
                .andExpect(jsonPath("$.quantity").value(supplyDto.getQuantity()));
    }

    @Test
    void testUpdateSupply() throws Exception {
        when(supplyService.updateSupply(anyLong(), any(SupplyDto.class))).thenReturn(supplyDto);

        mockMvc.perform(put("/api/supplies/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(supplyDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(supplyDto.getProductId()))
                .andExpect(jsonPath("$.quantity").value(supplyDto.getQuantity()));
    }

    @Test
    void testDeleteSupply() throws Exception {
        when(supplyService.deleteSupply(Long.valueOf("1"))).thenReturn(true);

        mockMvc.perform(delete("/api/supplies/1"))
                .andExpect(status().isNoContent());
    }
}
