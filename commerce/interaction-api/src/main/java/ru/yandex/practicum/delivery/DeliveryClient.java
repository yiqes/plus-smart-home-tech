package ru.yandex.practicum.delivery;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.order.OrderDto;

@FeignClient(name = "delivery", path = "/api/v1/delivery")
public interface DeliveryClient {
    @PostMapping("/cost")
    Double costDelivery(@RequestBody @Valid OrderDto orderDto);

    @PutMapping
    DeliveryDto createDelivery(@RequestBody @Valid DeliveryDto deliveryDto);
}