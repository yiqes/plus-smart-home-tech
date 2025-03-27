package ru.yandex.practicum.order;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Map;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductReturnRequestDto {
    @NotNull
    UUID orderId;
    @NotNull
    Map<UUID, Long> products;
}
