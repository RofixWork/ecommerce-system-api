package com.rofix.ecommerce_system.service;

import com.rofix.ecommerce_system.dto.response.OrderResponseDTO;
import com.rofix.ecommerce_system.security.service.UserDetailsImpl;
import jakarta.transaction.Transactional;

public interface OrderService {
    @Transactional
    OrderResponseDTO createOrder(UserDetailsImpl userDetails);
}
