package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.exception.NotAuthorizedUserException;
import ru.yandex.practicum.order.CreateNewOrderRequest;
import ru.yandex.practicum.order.OrderDto;
import ru.yandex.practicum.order.ProductReturnRequestDto;
import ru.yandex.practicum.service.OrderService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/order")
@Slf4j
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final String PATH_PAYMENT_FAILED = "/payment/failed";

    @GetMapping
    public List<OrderDto> getOrders(@RequestParam String username) {
        if (StringUtils.isEmpty(username)) throw new NotAuthorizedUserException("Username cannot be empty");
        log.info("Get orders by username {}", username);
        return orderService.getOrders(username);
    }

    @PutMapping
    public OrderDto createOrder(@RequestBody @Valid CreateNewOrderRequest newOrderRequest) {
        log.info("Create new order {}", newOrderRequest);
        return orderService.createOrder(newOrderRequest);
    }

    @PostMapping("/return")
    public OrderDto returnOrder(@RequestBody @Valid ProductReturnRequestDto returnRequest) {
        log.info("Return order {}", returnRequest);
        return orderService.returnOrder(returnRequest);
    }

    @PostMapping(value = "/payment")
    public OrderDto payOrder(@RequestBody UUID orderId) {
        log.info("Pay order {}", orderId);
        return orderService.payOrder(orderId);
    }

    @PostMapping(PATH_PAYMENT_FAILED)
    public OrderDto payOrderFailed(@RequestBody UUID orderId) {
        log.info("Pay order failed {}", orderId);
        return orderService.payOrderFailed(orderId);
    }

    @PostMapping("/delivery")
    public OrderDto deliveryOrder(@RequestBody UUID orderId) {
        log.info("Deliver order {}", orderId);
        return orderService.deliveryOrder(orderId);
    }

    @PostMapping("/delivery/failed")
    public OrderDto deliveryOrderFailed(@RequestBody UUID orderId) {
        log.info("Deliver order failed {}", orderId);
        return orderService.deliveryOrderFailed(orderId);
    }

    @PostMapping("/completed")
    public OrderDto completedOrder(@RequestBody UUID orderId) {
        log.info("Completed order {}", orderId);
        return orderService.completedOrder(orderId);
    }

    @PostMapping("/calculate/total")
    public OrderDto calculateTotal(@RequestBody UUID orderId) {
        log.info("Calculate total order {}", orderId);
        return orderService.calculateTotal(orderId);
    }

    @PostMapping("/calculate/delivery")
    public OrderDto calculateDelivery(@RequestBody UUID orderId) {
        log.info("Calculate delivery order {}", orderId);
        return orderService.calculateDelivery(orderId);
    }

    @PostMapping("/assembly")
    public OrderDto assemblyOrder(@RequestBody UUID orderId) {
        log.info("Assembly order {}", orderId);
        return orderService.assemblyOrder(orderId);
    }

    @PostMapping("/assembly/failed")
    public OrderDto assemblyOrderFailed(@RequestBody UUID orderId) {
        log.info("Assembly order failed {}", orderId);
        return orderService.assemblyOrderFailed(orderId);
    }

}