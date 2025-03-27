package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.exception.NotEnoughInfoException;
import ru.yandex.practicum.mapper.PaymentMapper;
import ru.yandex.practicum.model.Payment;
import ru.yandex.practicum.order.OrderDto;
import ru.yandex.practicum.order.feign.OrderClient;
import ru.yandex.practicum.payment.PaymentDto;
import ru.yandex.practicum.payment.PaymentStatus;
import ru.yandex.practicum.repository.PaymentRepository;
import ru.yandex.practicum.shoppingStore.ProductDto;
import ru.yandex.practicum.shoppingStore.ShoppingStoreClient;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {
    private final PaymentMapper paymentMapper;
    private final PaymentRepository paymentRepository;
    private final ShoppingStoreClient shoppingStoreClient;
    private final OrderClient orderClient;

    @Override
    public PaymentDto createPayment(OrderDto order) {
        checkOrder(order);
        Payment payment = Payment.builder()
                .orderId(order.getOrderId())
                .totalPayment(order.getTotalPrice())
                .deliveryTotal(order.getDeliveryPrice())
                .productsTotal(order.getProductPrice())
                .feeTotal(getTax(order.getTotalPrice()))
                .status(PaymentStatus.PENDING)
                .build();
        return paymentMapper.toPaymentDto(paymentRepository.save(payment));
    }

    @Override
    @Transactional(readOnly = true)
    public Double totalCost(OrderDto order) {
        if (order.getDeliveryPrice() == null) {
            throw new NotEnoughInfoException("Delivery price cannot be null");
        }
        return order.getProductPrice() + getTax(order.getProductPrice()) + order.getDeliveryPrice();
    }

    @Override
    public void refund(UUID uuid) {
        Payment payment = paymentRepository.findById(uuid).orElseThrow(
                () -> new NotEnoughInfoException("Payment with id = " + uuid + " not found")
        );
        payment.setStatus(PaymentStatus.SUCCESS);
        orderClient.payOrder(payment.getOrderId());
    }

    @Override
    @Transactional(readOnly = true)
    public Double productCost(OrderDto order) {
        double productCost = 0.0;
        Map<UUID, Long> products = order.getProducts();
        if (products == null) {
            throw new NotEnoughInfoException("Products cannot be null");
        }
        for (Map.Entry<UUID, Long> product : products.entrySet()) {
            ProductDto productDto = shoppingStoreClient.getProduct(product.getKey());
            productCost += productDto.getPrice() * product.getValue();
        }
        return productCost;
    }

    @Override
    public void failed(UUID uuid) {
        Payment payment = paymentRepository.findById(uuid).orElseThrow(
                () -> new NotEnoughInfoException("Payment with id = " + uuid + " not found")
        );
        payment.setStatus(PaymentStatus.FAILED);
        orderClient.payOrderFailed(payment.getOrderId());
    }

    private void checkOrder(OrderDto order) {
        if (order.getDeliveryPrice() == null) {
            throw new NotEnoughInfoException("Delivery price is null");
        } else if (order.getProductPrice() == null) {
            throw new NotEnoughInfoException("Product price is null");
        } else if (order.getTotalPrice() == null) {
            throw new NotEnoughInfoException("Total price is null");
        }
    }

    private double getTax(double totalPrice) {
        return totalPrice * 0.1;
    }
}
