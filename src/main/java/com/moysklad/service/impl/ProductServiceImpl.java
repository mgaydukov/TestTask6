package com.moysklad.service.impl;

import com.moysklad.dto.ProductDto;
import com.moysklad.exception.ProductNotFoundException;
import com.moysklad.mapper.ProductMapper;
import com.moysklad.model.Product;
import com.moysklad.repository.ProductRepository;
import com.moysklad.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;  // Маппер инжектируется через Spring

    public ProductServiceImpl(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    // Получаем все товары
    @Override
    public List<ProductDto> getAllProducts() {
        return productMapper.toProductDtoList(productRepository.findAll());
    }

    // Получаем товар по ID
    @Override
    public ProductDto getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product with ID " + id + " not found"));
        return productMapper.toProductDto(product);
    }

    // Создаем новый товар
    @Override
    public ProductDto createProduct(ProductDto productDto) {
        Product product = productMapper.toProduct(productDto);
        Product savedProduct = productRepository.save(product);
        return productMapper.toProductDto(savedProduct);
    }

    // Обновляем товар по ID
    @Override
    public ProductDto updateProduct(Long id, ProductDto productDto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product with ID " + id + " not found"));
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setInStock(productDto.isInStock());
        Product updatedProduct = productRepository.save(product);
        return productMapper.toProductDto(updatedProduct);
    }

    // Удаляем товар по ID
    @Override
    public boolean deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException("Product with ID " + id + " not found");
        }
        productRepository.deleteById(id);
        return true;
    }
}