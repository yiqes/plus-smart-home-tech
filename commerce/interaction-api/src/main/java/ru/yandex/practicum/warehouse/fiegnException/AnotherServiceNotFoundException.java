package ru.yandex.practicum.warehouse.fiegnException;

public class AnotherServiceNotFoundException extends RuntimeException {
    public AnotherServiceNotFoundException(String message) {
        super(message);
    }
}
