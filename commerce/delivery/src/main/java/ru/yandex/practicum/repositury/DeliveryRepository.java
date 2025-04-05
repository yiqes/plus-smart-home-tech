package ru.yandex.practicum.repositury;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.model.Delivery;

import java.util.Optional;
import java.util.UUID;

public interface DeliveryRepository extends JpaRepository<Delivery, UUID> {
    Optional<Delivery> findByOrderId(UUID orderId);
}
