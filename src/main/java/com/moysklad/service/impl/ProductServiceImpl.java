package com.moysklad.service.impl;

import com.moysklad.dto.ProductDto;
import com.moysklad.exception.ProductNotFoundException;
import com.moysklad.mapper.ProductMapper;
import com.moysklad.model.Product;
import com.moysklad.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final Map<Long, Product> products = new HashMap<>();  // Хранение в HashMap
    private final ProductMapper productMapper;  // Маппер инжектируется через Spring
    private long currentId = 1;

    public ProductServiceImpl(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    // Получаем все товары
    @Override
    public List<ProductDto> getAllProducts() {
        return products.values().stream()
                .map(productMapper::toProductDto)  // Используем маппер для преобразования
                .collect(Collectors.toList());
    }

    // Получаем товар по ID
    @Override
    public ProductDto getProductById(Long id) {
        Product product = products.get(id);
        if (product == null) {
            throw new ProductNotFoundException("Product with ID " + id + " not found");
        }
        return productMapper.toProductDto(product);
    }

    // Создаем новый товар
    @Override
    public ProductDto createProduct(ProductDto productDto) {
        // Генерация уникального ID для нового продукта
        if (productDto.getId() == null) {
            productDto.setId(currentId++);
        }
        // Преобразуем Dto в модель и сохраняем в HashMap
        Product product = productMapper.toProduct(productDto);
        products.put(product.getId(), product);

        // Возвращаем Dto после создания
        return productMapper.toProductDto(product);
    }

    // Обновляем товар по ID
    @Override
    public ProductDto updateProduct(Long id, ProductDto productDto) {
        Product product = products.get(id);
        if (product == null) {
            throw new ProductNotFoundException("Product with ID " + id + " not found");
        }
        // Обновляем данные товара
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setInStock(productDto.isInStock());

        // Возвращаем обновленное Dto
        return productMapper.toProductDto(product);
    }

    // Удаляем товар по ID
    @Override
    public boolean deleteProduct(Long id) {
        if (!products.containsKey(id)) {
            throw new ProductNotFoundException("Product with ID " + id + " not found");
        }
        products.remove(id);
        return true;
    }
}