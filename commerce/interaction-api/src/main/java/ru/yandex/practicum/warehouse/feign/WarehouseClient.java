package ru.yandex.practicum.warehouse.feign;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.shoppingCart.BookedProductsDto;
import ru.yandex.practicum.shoppingCart.ShoppingCartDto;

@FeignClient(name = "warehouse", url = "/api/v1/warehouse")
public interface WarehouseClient {
    @PostMapping("/booking")
    BookedProductsDto bookProduct(@Valid @RequestBody ShoppingCartDto shoppingCartDto);
}
