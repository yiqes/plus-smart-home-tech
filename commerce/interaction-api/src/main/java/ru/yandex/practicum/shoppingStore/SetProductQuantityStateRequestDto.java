package ru.yandex.practicum.shoppingStore;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.shoppingStore.enums.QuantityState;

import java.util.UUID;

@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SetProductQuantityStateRequestDto {
    @NotNull
    UUID productId;
    @NotNull
    QuantityState quantityState;
}
