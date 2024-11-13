package com.moysklad.controller;

import com.moysklad.dto.ProductDto;
import com.moysklad.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // Получаем все товары
    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        List<ProductDto> products = productService.getAllProducts();
        return ResponseEntity.ok(products);  // Возвращаем список товаров с HTTP 200
    }

    // Получаем товар по ID
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
        ProductDto product = productService.getProductById(id);
        if (product != null) {
            return ResponseEntity.ok(product);  // Товар найден, возвращаем с HTTP 200
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);  // Товар не найден, возвращаем HTTP 404
        }
    }

    // Создаем новый товар
    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody @Valid ProductDto productDto) {
        ProductDto createdProduct = productService.createProduct(productDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);  // Возвращаем созданный товар с HTTP 201
    }

    // Обновляем товар по ID
    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long id, @RequestBody @Valid ProductDto productDto) {
        try {
            ProductDto updatedProduct = productService.updateProduct(id, productDto);
            return ResponseEntity.ok(updatedProduct);  // Возвращаем обновленный товар с HTTP 200
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);  // Товар не найден, возвращаем HTTP 404
        }
    }

    // Удаляем товар по ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        boolean isDeleted = productService.deleteProduct(id);
        if (isDeleted) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();  // Возвращаем HTTP 204 если товар удален
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();  // Товар не найден, возвращаем HTTP 404
        }
    }
}
