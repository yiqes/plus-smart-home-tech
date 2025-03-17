package ru.yandex.practicum.mapper;

import org.mapstruct.Mapper;
import ru.yandex.practicum.model.Product;
import ru.yandex.practicum.shoppingStore.ProductDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDto productToProductDto(Product product);

    Product productDtoToProduct(ProductDto productDto);

    List<ProductDto> productsToProductDtos(List<Product> products);
}
