package com.moysklad.service;

import com.moysklad.dto.ProductDto;

import java.util.List;

public interface ProductService {
    List<ProductDto> getAllProducts();
    List<ProductDto> getAllProducts(String name, Double minPrice, Double maxPrice, Boolean inStock, String sortBy, String sortDirection, int limit);
    ProductDto getProductById(Long id);
    ProductDto createProduct(ProductDto productDto);
    ProductDto updateProduct(Long id, ProductDto productDto);
    boolean deleteProduct(Long id);
}