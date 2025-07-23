package com.rofix.ecommerce_system.dto.response;

import com.rofix.ecommerce_system.security.response.UserInfoResponse;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
public class ProductResponseDTO {
    private Long id;
    private String name;
    private String slug;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private CategoryResponseDTO category;
    private List<ProductImageResponseDTO> images;
    private CreatedByResponseDTO createdBy;
}
