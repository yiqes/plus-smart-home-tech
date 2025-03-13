package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.service.ShoppingCartService;
import ru.yandex.practicum.shoppingCart.BookedProductsDto;
import ru.yandex.practicum.shoppingCart.ChangeProductQuantityRequestDto;
import ru.yandex.practicum.shoppingCart.ShoppingCartDto;

import java.util.Map;
import java.util.UUID;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/shopping-carts")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @GetMapping
    public ShoppingCartDto getShoppingCart(@RequestParam String username) {
        log.info("Get shopping cart for user {}", username);
        return shoppingCartService.getShoppingCart(username);
    }

    @PutMapping
    public ShoppingCartDto addProductsToCart(@RequestParam String username,
                                             @RequestBody Map<UUID, Long> request) {
        log.info("Add products to shopping cart for user {}", username);
        return shoppingCartService.addProductsToCart(username, request);
    }

    @DeleteMapping
    public void deactivateShoppingCart(@RequestParam String username) {
        log.info("Delete shopping cart for user {}", username);
        shoppingCartService.deactivateShoppingCart(username);
    }

    @PostMapping("/remove")
    public ShoppingCartDto changeShoppingCart(@RequestParam String username,
                                              @RequestBody Map<UUID, Long> request) {
        log.info("Change shopping cart for user {}", username);
        return shoppingCartService.changeShoppingCart(username, request);
    }

    @PostMapping("/change-quantity")
    public ShoppingCartDto changeQuantityShoppingCart(@RequestParam String username,
                                                      @RequestBody @Valid ChangeProductQuantityRequestDto requestDto) {
        log.info("Change quantity shopping cart for user {}", username);
        return shoppingCartService.changeQuantityShoppingCart(username, requestDto);
    }

    @PostMapping("/booking")
    public BookedProductsDto bookingProducts(@RequestParam String username) {
        log.info("Booking shopping cart for user {}", username);
        return shoppingCartService.bookingProducts(username);
    }
}
