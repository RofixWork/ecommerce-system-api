package com.rofix.ecommerce_system.service;

import com.rofix.ecommerce_system.dto.request.CartRequestDTO;
import com.rofix.ecommerce_system.dto.response.CartResponseDTO;
import com.rofix.ecommerce_system.security.service.UserDetailsImpl;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

public interface CartService {
    @Transactional
    CartResponseDTO addProductFromCart(@Valid CartRequestDTO cartRequestDTO, UserDetailsImpl userDetails);

    CartResponseDTO getCartItems(UserDetailsImpl userDetails);

    @Transactional
    String clearCart(UserDetailsImpl userDetails);

    @Transactional
    String deleteProductFromCart(Long productId, UserDetailsImpl userDetails);
}
