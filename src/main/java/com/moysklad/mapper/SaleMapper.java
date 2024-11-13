package com.moysklad.mapper;

import com.moysklad.dto.SaleDto;
import com.moysklad.model.Sale;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SaleMapper {

    @Mapping(source = "product.id", target = "productId")
    SaleDto toSaleDto(Sale sale);

    @Mapping(source = "productId", target = "product.id")
    Sale toSale(SaleDto saleDTO);

    @Mapping(source = "productId", target = "product.id")
    List<SaleDto> toSaleDtoList(List<Sale> sale);
}
