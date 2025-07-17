package com.rofix.ecommerce_system.dto.response;

import lombok.*;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponseDTO {
    private Long id;
    private String name;
    private String description;
    private Instant createdAt;
    private Instant updatedAt;
}
