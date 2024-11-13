package com.moysklad.mapper;

import com.moysklad.dto.ProductDto;
import com.moysklad.model.Product;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    // Преобразование модели Product в ProductDto
    ProductDto toProductDto(Product product);

    // Преобразование ProductDto в модель Product
    Product toProduct(ProductDto productDto);

    // Преобразование списка Product в список ProductDto
    List<ProductDto> toProductDtoList(List<Product> products);
}
