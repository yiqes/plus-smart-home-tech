package ru.yandex.practicum.model;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Entity
@Table(name = "warehouse_product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Warehouse {
    @Id
    UUID productId;
    long quantity;
    boolean fragile;
    @Embedded
    Dimension dimension;
    double weight;
}