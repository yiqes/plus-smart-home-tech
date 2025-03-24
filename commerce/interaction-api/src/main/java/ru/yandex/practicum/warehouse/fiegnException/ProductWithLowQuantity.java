package ru.yandex.practicum.warehouse.fiegnException;

public class ProductWithLowQuantity extends RuntimeException {
    public ProductWithLowQuantity(String message) {
        super(message);
    }
}
