package ru.yandex.practicum.exception;

public class NotEnoughInfoException extends RuntimeException {
    public NotEnoughInfoException(String message) {
        super(message);
    }
}
