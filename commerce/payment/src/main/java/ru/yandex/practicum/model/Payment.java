package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.payment.PaymentStatus;

import java.util.UUID;

@Entity
@Table(name = "payments")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID paymentId;
    UUID orderId;
    double productsTotal;
    double deliveryTotal;
    double totalPayment;
    double feeTotal;
    @Enumerated(EnumType.STRING)
    PaymentStatus status;
}
