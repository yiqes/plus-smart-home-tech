package ru.yandex.practicum.service;

import ru.yandex.practicum.shoppingStore.PageableDto;
import ru.yandex.practicum.shoppingStore.ProductDto;
import ru.yandex.practicum.shoppingStore.SetProductQuantityStateRequestDto;
import ru.yandex.practicum.shoppingStore.enums.ProductCategory;

import java.util.List;
import java.util.UUID;

public interface ProductService {
    List<ProductDto> getProductsByCategory(ProductCategory productCategory, PageableDto pageableDto);

    ProductDto createProduct(ProductDto productDto);

    ProductDto updateProduct(ProductDto productDto);

    boolean deleteProduct(UUID productId);

    boolean updateQuantityState(SetProductQuantityStateRequestDto setProductQuantityStateRequestDto);

    ProductDto getProduct(UUID productId);
}
