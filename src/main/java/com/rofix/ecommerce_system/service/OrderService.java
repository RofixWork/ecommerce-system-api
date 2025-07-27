package com.rofix.ecommerce_system.service;

import com.rofix.ecommerce_system.dto.request.OrderStatusUpdateRequestDTO;
import com.rofix.ecommerce_system.dto.response.OrderResponseDTO;
import com.rofix.ecommerce_system.response.PageListResponse;
import com.rofix.ecommerce_system.security.service.UserDetailsImpl;
import jakarta.transaction.Transactional;

public interface OrderService {
    @Transactional
    OrderResponseDTO createOrder(UserDetailsImpl userDetails);

    OrderResponseDTO getOrderDetails(Long orderId, UserDetailsImpl userDetails);

    PageListResponse<OrderResponseDTO> getAllOrders(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, UserDetailsImpl userDetails);

    OrderResponseDTO updateOrderStatus(Long orderId, OrderStatusUpdateRequestDTO orderStatusUpdateRequestDTO, UserDetailsImpl userDetails);
}
