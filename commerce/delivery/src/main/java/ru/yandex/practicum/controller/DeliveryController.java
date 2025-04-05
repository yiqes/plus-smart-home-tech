package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.delivery.DeliveryDto;
import ru.yandex.practicum.order.OrderDto;
import ru.yandex.practicum.service.DeliveryService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/delivery")
@RequiredArgsConstructor
@Slf4j
public class DeliveryController {
    private final DeliveryService deliveryService;

    @PutMapping
    public DeliveryDto createDelivery(@RequestBody @Valid DeliveryDto deliveryDto) {
        log.info("Create delivery {}", deliveryDto);
        return deliveryService.createDelivery(deliveryDto);
    }

    @PostMapping("/successful")
    public void successfulDelivery(@RequestBody UUID deliveryId) {
        log.info("Successful delivery {}", deliveryId);
        deliveryService.successfulDelivery(deliveryId);
    }

    @PostMapping("/picked")
    public void pickedDelivery(@RequestBody UUID deliveryId) {
        log.info("Picked delivery {}", deliveryId);
        deliveryService.pickedDelivery(deliveryId);
    }

    @PostMapping("/failed")
    public void failedDelivery(@RequestBody UUID deliveryId) {
        log.info("Failed delivery {}", deliveryId);
        deliveryService.failedDelivery(deliveryId);
    }

    @PostMapping("/cost")
    public Double costDelivery(@RequestBody @Valid OrderDto orderDto) {
        log.info("Cost delivery {}", orderDto);
        return deliveryService.costDelivery(orderDto);
    }
}