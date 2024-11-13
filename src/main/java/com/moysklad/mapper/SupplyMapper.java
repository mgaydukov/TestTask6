package com.moysklad.mapper;

import com.moysklad.dto.SupplyDto;
import com.moysklad.model.Supply;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SupplyMapper {
    // Преобразование Supply в SupplyDto
    @Mapping(source = "product.id", target = "productId")
    SupplyDto toSupplyDto(Supply supply);

    // Преобразование SupplyDto в Supply
    @Mapping(source = "productId", target = "product.id")
    Supply toSupply(SupplyDto supplyDTO);

    @Mapping(source = "productId", target = "product.id")
    List<SupplyDto> toSupplyDtoList(List<Supply> Supply);
}
