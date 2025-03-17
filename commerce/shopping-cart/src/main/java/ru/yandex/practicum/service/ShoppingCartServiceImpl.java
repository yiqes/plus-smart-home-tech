package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.exception.NoProductsInShoppingCartException;
import ru.yandex.practicum.exception.NotAuthorizedUserException;
import ru.yandex.practicum.mapper.ShoppingCartMapper;
import ru.yandex.practicum.model.ShoppingCart;
import ru.yandex.practicum.repository.ShoppingCartRepository;
import ru.yandex.practicum.shoppingCart.BookedProductsDto;
import ru.yandex.practicum.shoppingCart.ChangeProductQuantityRequestDto;
import ru.yandex.practicum.shoppingCart.ShoppingCartDto;
import ru.yandex.practicum.warehouse.feign.WarehouseClient;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;

    private final WarehouseClient warehouseClient;

    @Override
    @Transactional(readOnly = true)
    public ShoppingCartDto getShoppingCart(String username) {
        checkUsername(username);
        ShoppingCart cart = shoppingCartRepository.findByUsername(username);
        return shoppingCartMapper.toShoppingCartDto(cart);
    }

    @Override
    public ShoppingCartDto addProductsToCart(String username, Map<UUID, Long> request) {
        checkUsername(username);
        ShoppingCart cart = ShoppingCart.builder()
                .username(username)
                .products(request)
                .active(true)
                .build();
        return shoppingCartMapper.toShoppingCartDto(shoppingCartRepository.save(cart));
    }

    @Override
    public void deactivateShoppingCart(String username) {
        checkUsername(username);
        ShoppingCart cart = shoppingCartRepository.findByUsername(username);
        cart.setActive(false);
    }

    @Override
    public ShoppingCartDto changeShoppingCart(String username, Map<UUID, Long> request) {
        checkUsername(username);
        ShoppingCart cart = shoppingCartRepository.findByUsername(username);
        if (cart == null) {
            throw new NoProductsInShoppingCartException(username + "'s is empty");
        }
        cart.setProducts(request);
        return shoppingCartMapper.toShoppingCartDto(cart);
    }

    @Override
    public ShoppingCartDto changeQuantityShoppingCart(String username, ChangeProductQuantityRequestDto requestDto) {
        checkUsername(username);
        ShoppingCart cart = shoppingCartRepository.findByUsername(username);
        cart.getProducts().entrySet().stream()
                .filter(entry -> entry.getKey().equals(requestDto.getProductId()))
                .peek(entry -> entry.setValue(requestDto.getNewQuantity()))
                .findAny()
                .orElseThrow(() -> new NoProductsInShoppingCartException(username + "'s shopping cart is empty"));
        shoppingCartRepository.save(cart);
        return shoppingCartMapper.toShoppingCartDto(cart);
    }

    @Override
    public BookedProductsDto bookingProducts(String username) {
        checkUsername(username);
        ShoppingCart cart = shoppingCartRepository.findByUsername(username);
        return warehouseClient.bookProduct(shoppingCartMapper.toShoppingCartDto(cart));
    }

    private void checkUsername(String username) {
        if (username == null || username.isEmpty()) {
            throw new NotAuthorizedUserException("Username cannot be null or empty");
        }
    }
}
