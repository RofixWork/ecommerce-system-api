package com.rofix.ecommerce_system.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderStatusUpdateRequestDTO {
    @NotBlank(message = "Order status cannot be empty or blank.")
    @Pattern(regexp = "^(?i)(new|paid|shipped)$", message = "Invalid order status. Allowed values are new, paid, or shipped.")
    private String status;
}
