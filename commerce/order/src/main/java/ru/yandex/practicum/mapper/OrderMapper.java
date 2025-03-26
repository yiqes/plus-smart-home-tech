package ru.yandex.practicum.mapper;

import org.mapstruct.Mapper;
import ru.yandex.practicum.model.Order;
import ru.yandex.practicum.order.OrderDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderDto toOrderDto(Order order);

    List<OrderDto> toOrderDtos(List<Order> orders);
}
