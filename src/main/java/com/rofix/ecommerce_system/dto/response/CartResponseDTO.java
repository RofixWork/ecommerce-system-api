package com.rofix.ecommerce_system.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CartResponseDTO {
    private Long id;
    private CreatedByResponseDTO user;
    List<CartItemResponseDTO> cartItems;
}
