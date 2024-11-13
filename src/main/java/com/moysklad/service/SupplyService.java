package com.moysklad.service;

import com.moysklad.dto.SupplyDto;

import java.util.List;

public interface SupplyService {
    List<SupplyDto> getAllSupplies();
    SupplyDto getSupplyById(Long id);
    SupplyDto createSupply(SupplyDto supplyDto);
    SupplyDto updateSupply(Long id, SupplyDto supplyDto);
    boolean deleteSupply(Long id);
}