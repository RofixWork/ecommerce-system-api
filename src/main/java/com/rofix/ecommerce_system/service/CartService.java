package com.rofix.ecommerce_system.service;

import com.rofix.ecommerce_system.dto.request.CartRequestDTO;
import com.rofix.ecommerce_system.dto.response.CartResponseDTO;
import com.rofix.ecommerce_system.response.PageListResponse;
import com.rofix.ecommerce_system.security.service.UserDetailsImpl;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

public interface CartService {
    @Transactional
    CartResponseDTO addProductFromCart(@Valid CartRequestDTO cartRequestDTO, UserDetailsImpl userDetails);


    CartResponseDTO getCartItems(UserDetailsImpl userDetails);
}
