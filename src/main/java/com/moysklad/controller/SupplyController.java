package com.moysklad.controller;

import com.moysklad.dto.SupplyDto;
import com.moysklad.service.SupplyService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/supplies")
public class SupplyController {

    private final SupplyService supplyService;

    public SupplyController(SupplyService supplyService) {
        this.supplyService = supplyService;
    }

    // Получаем все поставки
    @GetMapping
    public ResponseEntity<List<SupplyDto>> getAllSupplies() {
        List<SupplyDto> supplies = supplyService.getAllSupplies();
        return ResponseEntity.ok(supplies);  // Возвращаем список товаров с HTTP 200
    }

    // Получение информации о поставке по ID
    @GetMapping("/{id}")
    public ResponseEntity<SupplyDto> getSupplyById(@PathVariable Long id) {
        SupplyDto supplyDto = supplyService.getSupplyById(id);
        if (supplyDto != null) {
            return ResponseEntity.ok(supplyDto);  // Товар найден, возвращаем с HTTP 200
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);  // Товар не найден, возвращаем HTTP 404
        }
    }

    // Создание новой поставки
    @PostMapping
    public ResponseEntity<SupplyDto> createSupply(@RequestBody @Valid SupplyDto supplyDto) {
        SupplyDto createdSupply = supplyService.createSupply(supplyDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSupply);
    }

    // Обновление данных о поставке
    @PutMapping("/{id}")
    public ResponseEntity<SupplyDto> updateSupply(@PathVariable Long id, @RequestBody @Valid SupplyDto supplyDto) {
        try {
            SupplyDto updatedSupply = supplyService.updateSupply(id, supplyDto);
            return ResponseEntity.ok(updatedSupply);  // Возвращаем обновленный товар с HTTP 200
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);  // Товар не найден, возвращаем HTTP 404
        }
    }

    // Удаление поставки по ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupply(@PathVariable Long id) {
        boolean isDeleted = supplyService.deleteSupply(id);
        if (isDeleted) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();  // Возвращаем HTTP 204 если товар удален
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();  // Товар не найден, возвращаем HTTP 404
        }
    }
}