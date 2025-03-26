package ru.yandex.practicum.order;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.shoppingCart.ShoppingCartDto;
import ru.yandex.practicum.warehouse.dto.AddressDto;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateNewOrderRequest {
    @NotNull
    ShoppingCartDto shoppingCartDto;
    @NotNull
    AddressDto addressDto;
}
