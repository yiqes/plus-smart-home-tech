package ru.yandex.practicum.service;

import ru.yandex.practicum.shoppingCart.BookedProductsDto;
import ru.yandex.practicum.shoppingCart.ShoppingCartDto;
import ru.yandex.practicum.warehouse.dto.AddProductToWarehouseRequest;
import ru.yandex.practicum.warehouse.dto.AddressDto;
import ru.yandex.practicum.warehouse.dto.AssemblyProductForOrderFromShoppingCartDto;
import ru.yandex.practicum.warehouse.dto.NewProductInWarehouseRequestDto;

import java.util.Map;
import java.util.UUID;

public interface WarehouseService {
    void newProductToWarehouse(NewProductInWarehouseRequestDto requestDto);

    void returnProductToWarehouse(Map<UUID, Long> products);

    BookedProductsDto bookProduct(ShoppingCartDto shoppingCartDto);

    BookedProductsDto assemblyProductForOrder(AssemblyProductForOrderFromShoppingCartDto assemblyProductDto);

    AddressDto getAddress();

    void addProductToWarehouse(AddProductToWarehouseRequest requestDto);
}
