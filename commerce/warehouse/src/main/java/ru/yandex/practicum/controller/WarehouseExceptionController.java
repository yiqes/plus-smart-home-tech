package ru.yandex.practicum.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.common.ErrorResponse;
import ru.yandex.practicum.exception.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.exception.SpecifiedProductAlreadyInWarehouseException;
import ru.yandex.practicum.warehouse.fiegnException.ProductInShoppingCartLowQuantityInWarehouse;

import java.util.Arrays;

@RestControllerAdvice
@Slf4j
public class WarehouseExceptionController {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleCommonException(RuntimeException e) {
        log.error("500 {}", e.getMessage());
        return ErrorResponse.builder()
                .cause(e.getCause())
                .stackTrace(Arrays.asList(e.getStackTrace()))
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR.name())
                .userMessage(e.getMessage())
                .message("Internal Server Error")
                .suppressed(Arrays.asList(e.getSuppressed()))
                .localizedMessage(e.getLocalizedMessage())
                .build();
    }

    @ExceptionHandler({MissingServletRequestParameterException.class,
            MethodArgumentNotValidException.class,
            NoSpecifiedProductInWarehouseException.class,
            ProductInShoppingCartLowQuantityInWarehouse.class,
            SpecifiedProductAlreadyInWarehouseException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequestException(RuntimeException e) {
        log.error("400 {}", e.getMessage());
        return ErrorResponse.builder()
                .cause(e.getCause())
                .stackTrace(Arrays.asList(e.getStackTrace()))
                .httpStatus(HttpStatus.BAD_REQUEST.name())
                .userMessage(e.getMessage())
                .message("Bad request")
                .suppressed(Arrays.asList(e.getSuppressed()))
                .localizedMessage(e.getLocalizedMessage())
                .build();
    }

}