package com.rofix.ecommerce_system.dto.response;

import com.rofix.ecommerce_system.enums.OrderStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
public class OrderResponseDTO {
    private Long id;
    private CreatedByResponseDTO user;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    private BigDecimal totalPrice;
    private Instant createdAt;
    private List<OrderItemResponseDTO> orders;
}
