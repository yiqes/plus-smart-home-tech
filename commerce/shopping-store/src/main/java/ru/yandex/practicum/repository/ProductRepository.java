package ru.yandex.practicum.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.model.Product;
import ru.yandex.practicum.shoppingStore.enums.ProductCategory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {

    List<Product> findAllByProductCategory(ProductCategory productCategory, Pageable pageable);

    Optional<Product> findByProductId(UUID productId);
}
