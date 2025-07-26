package com.rofix.ecommerce_system.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
public class OrderItemResponseDTO {
    private Long id;
    private String name;
    private String slug;
    private Integer quantity;
    private BigDecimal price;
    private CategoryResponseDTO category;
    private List<ProductImageResponseDTO> images;
}
