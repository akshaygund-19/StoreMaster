package com.akshay.StoreMaster.service;

import com.akshay.StoreMaster.Constants.OrderStatus;
import com.akshay.StoreMaster.dto.OrderItemResponseDTO;
import com.akshay.StoreMaster.dto.OrderResponseDTO;
import com.akshay.StoreMaster.entity.Order;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderService {
    OrderResponseDTO placeOrder(Long userId);
    OrderResponseDTO updateOrderStatus(Long uerId, OrderStatus orderStatus);
    List<OrderResponseDTO> getMyOrders(Long orderId);
    List<OrderResponseDTO> getAllOrders();
}
