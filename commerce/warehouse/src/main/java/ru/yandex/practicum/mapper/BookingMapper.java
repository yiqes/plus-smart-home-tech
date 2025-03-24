package ru.yandex.practicum.mapper;

import org.mapstruct.Mapper;
import ru.yandex.practicum.model.Booking;
import ru.yandex.practicum.shoppingCart.BookedProductsDto;

@Mapper(componentModel = "spring")
public interface BookingMapper {
    BookedProductsDto toBookedProductsDto(Booking booking);
}
