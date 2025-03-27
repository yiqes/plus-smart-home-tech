package ru.yandex.practicum.order.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.order.OrderDto;

import java.util.UUID;

@FeignClient(name = "order", path = "/api/v1/order")
public interface OrderClient {
    String PATH_PAYMENT_FAILED = "/payment/failed";
    String PATH_PAYMENT = "/payment";
    String PATH_COMPLETED = "/completed";
    String PATH_ASSEMBLY = "/assembly";
    String PATH_ASSEMBLY_FAILED = "/assembly/failed";
    String PATH_DELIVERY_FAILED = "/assembly/failed";

    @PostMapping(value = PATH_PAYMENT)
    OrderDto payOrder(@RequestBody UUID orderId);

    @PostMapping(PATH_PAYMENT_FAILED)
    OrderDto payOrderFailed(@RequestBody UUID orderId);

    @PostMapping(PATH_COMPLETED)
    OrderDto completedOrder(@RequestBody UUID orderId);

    @PostMapping(PATH_ASSEMBLY)
    OrderDto assemblyOrder(@RequestBody UUID orderId);

    @PostMapping(PATH_ASSEMBLY_FAILED)
    OrderDto assemblyOrderFailed(@RequestBody UUID orderId);

    @PostMapping(PATH_DELIVERY_FAILED)
    OrderDto deliveryOrderFailed(@RequestBody UUID orderId);
}
