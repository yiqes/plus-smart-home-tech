package ru.yandex.practicum.warehouse.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AssemblyProductForOrderFromShoppingCartDto {
    @NotNull
    UUID shoppingCartId;
    @NotNull
    UUID orderId;
}