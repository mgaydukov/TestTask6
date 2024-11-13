package com.moysklad.service;

import com.moysklad.dto.SaleDto;

import java.util.List;

public interface SaleService {
    List<SaleDto> getAllSales();
    SaleDto getSaleById(Long id);
    SaleDto createSale(SaleDto saleDto);
    SaleDto updateSale(Long id, SaleDto saleDto);
    boolean deleteSale(Long id);
}