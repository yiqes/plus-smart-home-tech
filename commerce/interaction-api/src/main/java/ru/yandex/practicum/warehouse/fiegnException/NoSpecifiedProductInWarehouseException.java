package ru.yandex.practicum.warehouse.fiegnException;

public class NoSpecifiedProductInWarehouseException extends RuntimeException {
    public NoSpecifiedProductInWarehouseException(String message) {
        super(message);
    }
}
