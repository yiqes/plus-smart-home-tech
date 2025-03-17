package ru.yandex.practicum.service;

import ru.yandex.practicum.shoppingCart.BookedProductsDto;
import ru.yandex.practicum.shoppingCart.ChangeProductQuantityRequestDto;
import ru.yandex.practicum.shoppingCart.ShoppingCartDto;

import java.util.Map;
import java.util.UUID;

public interface ShoppingCartService {
    ShoppingCartDto getShoppingCart(String username);

    ShoppingCartDto addProductsToCart(String username, Map<UUID, Long> request);

    void deactivateShoppingCart(String username);

    ShoppingCartDto changeShoppingCart(String username, Map<UUID, Long> request);

    ShoppingCartDto changeQuantityShoppingCart(String username, ChangeProductQuantityRequestDto requestDto);

    BookedProductsDto bookingProducts(String username);
}