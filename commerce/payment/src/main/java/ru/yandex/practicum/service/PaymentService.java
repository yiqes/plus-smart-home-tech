package ru.yandex.practicum.service;


import ru.yandex.practicum.order.OrderDto;
import ru.yandex.practicum.payment.PaymentDto;

import java.util.UUID;

public interface PaymentService {
    PaymentDto createPayment(OrderDto order);

    Double totalCost(OrderDto order);

    void refund(UUID uuid);

    Double productCost(OrderDto order);

    void failed(UUID uuid);
}