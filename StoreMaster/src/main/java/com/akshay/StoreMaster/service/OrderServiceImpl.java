package com.akshay.StoreMaster.service;

import com.akshay.StoreMaster.Constants.OrderStatus;
import com.akshay.StoreMaster.dto.OrderItemResponseDTO;
import com.akshay.StoreMaster.dto.OrderResponseDTO;
import com.akshay.StoreMaster.entity.Cart;
import com.akshay.StoreMaster.entity.Order;
import com.akshay.StoreMaster.entity.OrderItem;
import com.akshay.StoreMaster.entity.Product;
import com.akshay.StoreMaster.repository.CartRepository;
import com.akshay.StoreMaster.repository.OrderRepository;
import com.akshay.StoreMaster.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;

    public OrderServiceImpl(OrderRepository orderRepository, ProductRepository productRepository,
                            CartRepository cartRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
    }

    @Override
    public OrderResponseDTO placeOrder(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        if (cart.getCartItemList().isEmpty()){
            throw new RuntimeException("Cart is Empty");
        }

        Order order = new Order();
        order.setUserId(userId);
        order.setStatus(OrderStatus.PENDING);

        List<OrderItem> orderItems = cart.getCartItemList().stream()
                .map(cartItem -> {
                    Product product = productRepository.findById(cartItem.getProduct().getProduct_Id())
                            .orElseThrow(() -> new RuntimeException("Product Not found"));

                    if (product.getStock_quantity() < cartItem.getQuantity()){
                        throw new RuntimeException("Insufficient stock for product" + product.getName());
                    }
                    product.setStock_quantity(product.getStock_quantity() - cartItem.getQuantity());
                    productRepository.save(product);

                    OrderItem item =  new OrderItem();
                    item.setProductId(cartItem.getProduct().getProduct_Id());
                    item.setQuantity(cartItem.getQuantity());
                    item.setPrice(product.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
                    item.setOrder(order);
                    return item;
                }).toList();

        order.setItems(orderItems);

        BigDecimal total = orderItems.stream()
                .map(OrderItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotal(total);
        order.setCreatedAt(LocalDateTime.now());

        Order savedOrder = orderRepository.save(order);

        cart.getCartItemList().clear();
        cartRepository.save(cart);
        return mapToOrderResponseDTO(savedOrder);
    }

    @Override
    public OrderResponseDTO updateOrderStatus(Long orderId, OrderStatus orderStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order Not found"));
        order.setStatus(orderStatus);
        Order updatedOrder = orderRepository.save(order);
        return mapToOrderResponseDTO(updatedOrder);
    }

    @Override
    @Transactional
    public List<OrderResponseDTO> getMyOrders(Long userId) {
        return orderRepository.findByUserId(userId).stream()
                .map(this::mapToOrderResponseDTO)
                .toList();
    }

    @Override
    @Transactional
    public List<OrderResponseDTO> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::mapToOrderResponseDTO)
                .toList();
    }

    private OrderResponseDTO mapToOrderResponseDTO(Order order) {
        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setOrderId(order.getId());
        dto.setUserId(order.getUserId());
        dto.setTotal(order.getTotal());
        dto.setStatus(order.getStatus());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setItems(order.getItems().stream()
                .map(item -> new OrderItemResponseDTO(
                        item.getId(),
                        item.getQuantity(),
                        item.getPrice()
                ))
                .toList());
        return dto;
    }
}
