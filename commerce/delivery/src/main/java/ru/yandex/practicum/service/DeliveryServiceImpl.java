package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.DeliveryNotFoundException;
import ru.yandex.practicum.delivery.DeliveryDto;
import ru.yandex.practicum.delivery.DeliveryState;
import ru.yandex.practicum.mapper.DeliveryMapper;
import ru.yandex.practicum.model.Delivery;
import ru.yandex.practicum.order.OrderDto;
import ru.yandex.practicum.order.feign.OrderClient;
import ru.yandex.practicum.repositury.DeliveryRepository;
import ru.yandex.practicum.warehouse.dto.AddressDto;
import ru.yandex.practicum.warehouse.dto.ShippedToDeliveryRequest;
import ru.yandex.practicum.warehouse.feign.WarehouseClient;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class DeliveryServiceImpl implements DeliveryService {
    private final DeliveryMapper deliveryMapper;
    private final DeliveryRepository deliveryRepository;
    private final OrderClient orderClient;
    private final WarehouseClient warehouseClient;

    @Override
    public DeliveryDto createDelivery(DeliveryDto deliveryDto) {
        Delivery delivery = deliveryMapper.toDelivery(deliveryDto);
        delivery.setDeliveryState(DeliveryState.CREATED);
        return deliveryMapper.toDeliveryDto(deliveryRepository.save(delivery));
    }

    @Override
    public void successfulDelivery(UUID deliveryId) {
        Delivery delivery = deliveryRepository.findById(deliveryId).orElseThrow(
                () -> new DeliveryNotFoundException("Delivery with id = " + deliveryId + " not found")
        );
        delivery.setDeliveryState(DeliveryState.DELIVERED);
        orderClient.completedOrder(delivery.getOrderId());
    }

    @Override
    public void pickedDelivery(UUID deliveryId) {
        Delivery delivery = deliveryRepository.findById(deliveryId).orElseThrow(
                () -> new DeliveryNotFoundException("Delivery with id = " + deliveryId + " not found")
        );
        delivery.setDeliveryState(DeliveryState.IN_PROGRESS);
        orderClient.assemblyOrder(delivery.getOrderId());
        ShippedToDeliveryRequest deliveryRequest = new ShippedToDeliveryRequest(
                delivery.getOrderId(), delivery.getDeliveryId()
        );
        warehouseClient.shippedToDelivery(deliveryRequest);
    }

    @Override
    public void failedDelivery(UUID deliveryId) {
        Delivery delivery = deliveryRepository.findById(deliveryId).orElseThrow(
                () -> new DeliveryNotFoundException("Delivery with id = " + deliveryId + " not found")
        );
        delivery.setDeliveryState(DeliveryState.FAILED);
        orderClient.deliveryOrderFailed(delivery.getOrderId());
    }

    @Override
    @Transactional(readOnly = true)
    public Double costDelivery(OrderDto orderDto) {
        final double baseRate = 5.0;

        Delivery delivery = deliveryRepository.findByOrderId(orderDto.getOrderId()).orElseThrow(
                () -> new DeliveryNotFoundException(String.format("Delivery with order id %s not found", orderDto.getOrderId()))
        );

        AddressDto warehouseAddress = warehouseClient.getAddress();
        double addressCost = switch (warehouseAddress.getCity()) {
            case "ADDRESS_1" -> baseRate * 1;
            case "ADDRESS_2" -> baseRate * 2;
            default -> throw new IllegalStateException(String.format("Unexpected value: %s", warehouseAddress.getCity()));
        };
        double deliveryCost = baseRate + addressCost;
        if (orderDto.getFragile()) deliveryCost += deliveryCost * 0.2;
        deliveryCost += orderDto.getDeliveryWeight() * 0.3;
        deliveryCost += orderDto.getDeliveryVolume() * 0.2;
        if (!warehouseAddress.getStreet().equals(delivery.getToAddress().getStreet())) {
            deliveryCost += deliveryCost * 0.2;
        }
        return deliveryCost;
    }
}
