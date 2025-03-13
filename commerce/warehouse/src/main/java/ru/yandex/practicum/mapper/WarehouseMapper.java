package ru.yandex.practicum.mapper;

import org.mapstruct.Mapper;
import ru.yandex.practicum.model.Warehouse;
import ru.yandex.practicum.warehouse.dto.NewProductInWarehouseRequestDto;

@Mapper(componentModel = "spring")
public interface WarehouseMapper {
    Warehouse toWarehouse(NewProductInWarehouseRequestDto newProductInWarehouseRequestDto);
}
