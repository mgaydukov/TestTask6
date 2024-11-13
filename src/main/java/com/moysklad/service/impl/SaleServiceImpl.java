package com.moysklad.service.impl;

import com.moysklad.dto.SaleDto;
import com.moysklad.exception.ProductNotFoundException;
import com.moysklad.exception.SaleNotFoundException;
import com.moysklad.mapper.SaleMapper;
import com.moysklad.model.Product;
import com.moysklad.model.Sale;
import com.moysklad.repository.ProductRepository;
import com.moysklad.repository.SaleRepository;
import com.moysklad.service.SaleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SaleServiceImpl implements SaleService {

    private final ProductRepository productRepository;
    private final SaleRepository saleRepository;
    private final SaleMapper saleMapper;

    public SaleServiceImpl(ProductRepository productRepository, SaleRepository saleRepository, SaleMapper saleMapper) {
        this.productRepository = productRepository;
        this.saleRepository = saleRepository;
        this.saleMapper = saleMapper;
    }

    // Получаем продажи
    @Override
    public List<SaleDto> getAllSales() {
        return saleMapper.toSaleDtoList(saleRepository.findAll());
    }

    // Получение информации о продаже по ID
    @Override
    public SaleDto getSaleById(Long id) {
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new SaleNotFoundException("Sale with ID " + id + " not found"));
        return saleMapper.toSaleDto(sale);
    }

    // Создание новой продажи
    @Override
    public SaleDto createSale(SaleDto saleDto) {
        // Найти товар по ID
        Product product = productRepository.findById(saleDto.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Product does not exist"));

        // Проверить, достаточно ли товара на складе
        if (product.getStock() < saleDto.getQuantity()) {
            throw new IllegalArgumentException("Insufficient stock for sale");
        }

        Sale sale = saleMapper.toSale(saleDto);
        sale.setProduct(product);

        // Уменьшить количество товара на складе
        product.setStock(product.getStock() - sale.getQuantity());
        if (product.getStock() < 1)
            product.setInStock(false);
        productRepository.save(product);

        Sale savedSale = saleRepository.save(sale);
        return saleMapper.toSaleDto(savedSale);
    }

    // Обновление данных о продаже
    @Override
    public SaleDto updateSale(Long id, SaleDto saleDto) {
        Sale existingSale = saleRepository.findById(id)
                .orElseThrow(() -> new SaleNotFoundException("Sale with ID " + id + " not found"));

        Product product = productRepository.findById(saleDto.getProductId())
                .orElseThrow(() -> new ProductNotFoundException("Product with ID " + saleDto.getProductId() + " not found"));

        if (saleDto.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }

        int stockAdjustment = saleDto.getQuantity() - existingSale.getQuantity();

        if (product.getStock() < stockAdjustment) {
            throw new IllegalArgumentException("Insufficient stock for product with ID " + saleDto.getProductId());
        }

        product.setStock(product.getStock() - stockAdjustment);
        productRepository.save(product);

        existingSale.setDocumentName(saleDto.getDocumentName());
        existingSale.setProduct(product);
        existingSale.setQuantity(saleDto.getQuantity());
        existingSale.setPrice(saleDto.getPrice());

        Sale updatedSale = saleRepository.save(existingSale);
        return saleMapper.toSaleDto(updatedSale);
    }

    // Удаление продажи по ID
    @Override
    public boolean deleteSale(Long id) {
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new SaleNotFoundException("Sale with ID " + id + " not found"));
        saleRepository.delete(sale);

        return true;
    }
}

