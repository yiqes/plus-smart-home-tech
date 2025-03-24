package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.service.ProductService;
import ru.yandex.practicum.shoppingStore.PageableDto;
import ru.yandex.practicum.shoppingStore.ProductDto;
import ru.yandex.practicum.shoppingStore.SetProductQuantityStateRequestDto;
import ru.yandex.practicum.shoppingStore.enums.ProductCategory;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/shopping-store")
@Slf4j
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public List<ProductDto> getProductsByCategory(@RequestParam ProductCategory productCategory,
                                                  @Valid PageableDto pageableDto) {
        log.info("Get products by category {}", productCategory);
        return productService.getProductsByCategory(productCategory, pageableDto);
    }

    @PutMapping
    public ProductDto createProduct(@RequestBody @Valid ProductDto productDto) {
        log.info("Create product {}", productDto);
        return productService.createProduct(productDto);
    }

    @PostMapping
    public ProductDto updateProduct(@RequestBody @Valid ProductDto productDto) {
        log.info("Update product {}", productDto);
        return productService.updateProduct(productDto);
    }

    @PostMapping("/removeProductFromStore")
    public Boolean deleteProduct(@RequestBody @NotNull UUID productId) {
        log.info("Delete product {}", productId);
        return productService.deleteProduct(productId);
    }

    @PostMapping("/quantity/state")
    public Boolean updateProductQuantityState(@Valid SetProductQuantityStateRequestDto setProductQuantityStateRequestDto) {
        log.info("Update quantity state {}", setProductQuantityStateRequestDto);
        return productService.updateQuantityState(setProductQuantityStateRequestDto);
    }

    @GetMapping("/{product-id}")
    public ProductDto getProduct(@PathVariable("user-id") @NotNull UUID productId) {
        log.info("Get product by id: {}", productId);
        return productService.getProduct(productId);
    }
}
