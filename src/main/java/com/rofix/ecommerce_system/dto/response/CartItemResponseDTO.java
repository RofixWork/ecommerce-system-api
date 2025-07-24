package com.rofix.ecommerce_system.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
public class CartItemResponseDTO {
    private Long id;
    private String name;
    private String slug;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal itemTotal;
    private CategoryResponseDTO category;
    private List<ProductImageResponseDTO> images;
}
