package ru.yandex.practicum.payment;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.order.OrderDto;

@FeignClient(name = "payment", path = "/api/v1/payment")
public interface PaymentClient {
    @PostMapping("/totalCost")
    Double totalCost(@RequestBody @Valid OrderDto order);

    @PostMapping("/productCost")
    Double productCost(@RequestBody @Valid OrderDto order);

    @PostMapping
    PaymentDto createPayment(@RequestBody @Valid OrderDto order);
}