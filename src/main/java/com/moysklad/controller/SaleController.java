package com.moysklad.controller;

import com.moysklad.dto.SaleDto;
import com.moysklad.service.SaleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sales")
public class SaleController {

    private final SaleService saleService;

    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    // Получаем продажи
    @GetMapping
    public ResponseEntity<List<SaleDto>> getAllSales() {
        List<SaleDto> sales = saleService.getAllSales();
        return ResponseEntity.ok(sales);  // Возвращаем список товаров с HTTP 200
    }

    // Получение информации о продаже по ID
    @GetMapping("/{id}")
    public ResponseEntity<SaleDto> getSaleById(@PathVariable Long id) {
        SaleDto saleDto = saleService.getSaleById(id);
        if (saleDto != null) {
            return ResponseEntity.ok(saleDto);  // Товар найден, возвращаем с HTTP 200
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);  // Товар не найден, возвращаем HTTP 404
        }
    }

    // Создание новой продажи
    @PostMapping
    public ResponseEntity<SaleDto> createSale(@RequestBody @Valid SaleDto saleDto) {
        SaleDto createdSale = saleService.createSale(saleDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSale);
    }

    // Обновление данных о продаже
    @PutMapping("/{id}")
    public ResponseEntity<SaleDto> updateSale(@PathVariable Long id, @RequestBody @Valid SaleDto saleDto) {
        try {
            SaleDto updatedSale = saleService.updateSale(id, saleDto);
            return ResponseEntity.ok(updatedSale);  // Возвращаем обновленный товар с HTTP 200
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);  // Товар не найден, возвращаем HTTP 404
        }
    }

    // Удаление продажи по ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSale(@PathVariable Long id) {
        boolean isDeleted = saleService.deleteSale(id);
        if (isDeleted) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();  // Возвращаем HTTP 204 если товар удален
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();  // Товар не найден, возвращаем HTTP 404
        }
    }
}