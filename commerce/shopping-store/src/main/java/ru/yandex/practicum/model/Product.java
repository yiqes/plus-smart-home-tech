package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.shoppingStore.enums.ProductCategory;
import ru.yandex.practicum.shoppingStore.enums.ProductState;
import ru.yandex.practicum.shoppingStore.enums.QuantityState;

import java.util.UUID;

@Entity
@Table(name = "products")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    UUID productId;
    String productName;
    String description;
    String imageSrc;
    @Enumerated(EnumType.STRING)
    QuantityState quantityState;
    @Enumerated(EnumType.STRING)
    ProductState productState;
    Integer rating;
    @Enumerated(EnumType.STRING)
    ProductCategory productCategory;
    Double price;
}
