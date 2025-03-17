package ru.yandex.practicum.exception;

public class HandleNotFound extends RuntimeException{
    public HandleNotFound(String message) {
        super(message);
    }
}