package com.rofix.ecommerce_system.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CartRequestDTO {
    @NotNull(message = "Quantity is required.")
    @Min(value = 1, message = "Quantity must be at least 1.")
    private Integer quantity;

    @NotNull(message = "Product ID is required.")
    @Min(value = 1, message = "Product ID must be at least 1.")
    private Long productId;
}
