package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.delivery.DeliveryClient;
import ru.yandex.practicum.delivery.DeliveryDto;
import ru.yandex.practicum.exception.OrderNotFoundException;
import ru.yandex.practicum.mapper.OrderMapper;
import ru.yandex.practicum.model.Order;
import ru.yandex.practicum.order.CreateNewOrderRequest;
import ru.yandex.practicum.order.OrderDto;
import ru.yandex.practicum.order.ProductReturnRequestDto;
import ru.yandex.practicum.order.enums.OrderState;
import ru.yandex.practicum.payment.PaymentClient;
import ru.yandex.practicum.repository.OrderRepository;
import ru.yandex.practicum.shoppingCart.BookedProductsDto;
import ru.yandex.practicum.shoppingCart.ShoppingCartClient;
import ru.yandex.practicum.shoppingCart.ShoppingCartDto;
import ru.yandex.practicum.shoppingStore.ShoppingStoreClient;
import ru.yandex.practicum.warehouse.dto.AssemblyProductForOrderFromShoppingCartDto;
import ru.yandex.practicum.warehouse.feign.WarehouseClient;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(isolation = Isolation.READ_COMMITTED)
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final ShoppingCartClient shoppingCartClient;
    private final ShoppingStoreClient shoppingStoreClient;
    private final WarehouseClient warehouseClient;
    private final PaymentClient paymentClient;
    private final DeliveryClient deliveryClient;

    @Override
    @Transactional(readOnly = true)
    public List<OrderDto> getOrders(String username) {
        ShoppingCartDto shoppingCartDto = shoppingCartClient.getShoppingCart(username);
        List<Order> orders = orderRepository.findByShoppingCartId(shoppingCartDto.getShoppingCartId());
        return orderMapper.toOrderDtos(orders);
    }

    @Override
    public OrderDto createOrder(CreateNewOrderRequest newOrderRequest) {
        Order order = Order.builder()
                .shoppingCartId(newOrderRequest.getShoppingCartDto().getShoppingCartId())
                .products(newOrderRequest.getShoppingCartDto().getProducts())
                .state(OrderState.NEW)
                .build();
        Order newOrder = orderRepository.save(order);
        BookedProductsDto bookedProducts = warehouseClient.assemblyProductForOrder(
                new AssemblyProductForOrderFromShoppingCartDto(
                        newOrderRequest.getShoppingCartDto().getShoppingCartId(),
                        newOrder.getOrderId()
                ));

        newOrder.setFragile(bookedProducts.getFragile());
        newOrder.setDeliveryVolume(bookedProducts.getDeliveryVolume());
        newOrder.setDeliveryWeight(bookedProducts.getDeliveryWeight());
        newOrder.setProductPrice(paymentClient.productCost(orderMapper.toOrderDto(newOrder)));

        DeliveryDto deliveryDto = DeliveryDto.builder()
                .orderId(newOrder.getOrderId())
                .fromAddress(warehouseClient.getAddress())
                .toAddress(newOrderRequest.getAddressDto())
                .build();
        newOrder.setDeliveryId(deliveryClient.createDelivery(deliveryDto).getDeliveryId());

        paymentClient.createPayment(orderMapper.toOrderDto(newOrder));

        return orderMapper.toOrderDto(newOrder);
    }

    @Override
    public OrderDto returnOrder(ProductReturnRequestDto returnRequest) {
        Order order = orderRepository.findById(returnRequest.getOrderId())
                .orElseThrow(() -> new OrderNotFoundException(String.format("Order not found with id %s", returnRequest.getOrderId())));
        warehouseClient.returnProductToWarehouse(returnRequest.getProducts());
        order.setState(OrderState.PRODUCT_RETURNED);
        return orderMapper.toOrderDto(order);
    }

    @Override
    public OrderDto payOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(String.format("Order not found with id %s", orderId)));
        order.setState(OrderState.PAID);
        return orderMapper.toOrderDto(order);
    }

    @Override
    public OrderDto payOrderFailed(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(String.format("Order not found with id %s", orderId)));
        order.setState(OrderState.PAYMENT_FAILED);
        return orderMapper.toOrderDto(order);
    }

    @Override
    public OrderDto deliveryOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(String.format("Order not found with id %s", orderId)));
        order.setState(OrderState.DELIVERED);
        return orderMapper.toOrderDto(order);
    }

    @Override
    public OrderDto deliveryOrderFailed(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(String.format("Order not found with id %s", orderId)));
        order.setState(OrderState.DELIVERY_FAILED);
        return orderMapper.toOrderDto(order);
    }

    @Override
    public OrderDto completedOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(String.format("Order not found with id %s", orderId)));
        order.setState(OrderState.COMPLETED);
        return orderMapper.toOrderDto(order);
    }

    @Override
    public OrderDto calculateTotal(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(String.format("Order not found with id %s", orderId)));
        order.setTotalPrice(paymentClient.totalCost(orderMapper.toOrderDto(order)));
        return orderMapper.toOrderDto(order);
    }

    @Override
    public OrderDto calculateDelivery(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(String.format("Order not found with id %s", orderId)));
        order.setDeliveryPrice(deliveryClient.costDelivery(orderMapper.toOrderDto(order)));
        return orderMapper.toOrderDto(order);
    }

    @Override
    public OrderDto assemblyOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(String.format("Order not found with id %s", orderId)));
        order.setState(OrderState.ASSEMBLED);
        return orderMapper.toOrderDto(order);
    }

    @Override
    public OrderDto assemblyOrderFailed(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(String.format("Order not found with id %s", orderId)));
        order.setState(OrderState.ASSEMBLY_FAILED);
        return orderMapper.toOrderDto(order);
    }
}
