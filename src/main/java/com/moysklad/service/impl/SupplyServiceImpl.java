package com.moysklad.service.impl;

import com.moysklad.dto.SupplyDto;
import com.moysklad.exception.ProductNotFoundException;
import com.moysklad.exception.SupplyNotFoundException;
import com.moysklad.mapper.SupplyMapper;
import com.moysklad.model.Product;
import com.moysklad.model.Supply;
import com.moysklad.repository.ProductRepository;
import com.moysklad.repository.SupplyRepository;
import com.moysklad.service.SupplyService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupplyServiceImpl implements SupplyService {

    private final ProductRepository productRepository;
    private final SupplyRepository supplyRepository;
    private final SupplyMapper supplyMapper;

    public SupplyServiceImpl(ProductRepository productRepository, SupplyRepository supplyRepository, SupplyMapper supplyMapper) {
        this.productRepository = productRepository;
        this.supplyRepository = supplyRepository;
        this.supplyMapper = supplyMapper;
    }

    // Получаем все поставки
    @Override
    public List<SupplyDto> getAllSupplies() {
        return supplyMapper.toSupplyDtoList(supplyRepository.findAll());
    }

    // Получение информации о поставке по ID
    @Override
    public SupplyDto getSupplyById(Long id) {
        Supply supply = supplyRepository.findById(id)
                .orElseThrow(() -> new SupplyNotFoundException("Supply with ID " + id + " not found"));
        return supplyMapper.toSupplyDto(supply);
    }

    // Создание новой поставки
    @Override
    public SupplyDto createSupply(SupplyDto supplyDto) {
        // Найти товар по ID
        Product product = productRepository.findById(supplyDto.getProductId())
                .orElseThrow(() -> new ProductNotFoundException("Product with ID " + supplyDto.getProductId() + " not found"));

        if (supplyDto.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }

        // Увеличиваем количество товара на складе
        product.setStock(product.getStock() + supplyDto.getQuantity());
        if (product.getStock() > 0)
            product.setInStock(true);
        productRepository.save(product);

        Supply supply = supplyMapper.toSupply(supplyDto);
        supply.setProduct(product);

        Supply savedSupply = supplyRepository.save(supply);
        return supplyMapper.toSupplyDto(savedSupply);
    }

    // Обновление данных о поставке
    @Override
    public SupplyDto updateSupply(Long id, SupplyDto supplyDto) {
        Supply existingSupply = supplyRepository.findById(id)
                .orElseThrow(() -> new SupplyNotFoundException("Supply with ID " + id + " not found"));

        Product product = productRepository.findById(supplyDto.getProductId())
                .orElseThrow(() -> new ProductNotFoundException("Product with ID " + supplyDto.getProductId() + " not found"));

        if (supplyDto.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }

        // Корректируем запас товара на складе
        int quantityAdjustment = supplyDto.getQuantity() - existingSupply.getQuantity();
        product.setStock(product.getStock() + quantityAdjustment);
        productRepository.save(product);

        existingSupply.setDocumentName(supplyDto.getDocumentName());
        existingSupply.setProduct(product);
        existingSupply.setQuantity(supplyDto.getQuantity());

        Supply updatedSupply = supplyRepository.save(existingSupply);
        return supplyMapper.toSupplyDto(updatedSupply);
    }

    // Удаление поставки по ID
    @Override
    public boolean deleteSupply(Long id) {
        Supply supply = supplyRepository.findById(id)
                .orElseThrow(() -> new SupplyNotFoundException("Supply with ID " + id + " not found"));

        // Уменьшаем запас товара при удалении поставки
        Product product = supply.getProduct();
        product.setStock(product.getStock() - supply.getQuantity());
        productRepository.save(product);

        supplyRepository.delete(supply);
        return true;
    }
}

