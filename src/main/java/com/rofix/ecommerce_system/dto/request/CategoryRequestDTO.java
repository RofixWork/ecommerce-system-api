package com.rofix.ecommerce_system.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequestDTO {
    @NotBlank(message = "category name cannot be blank!!!")
    @Size(min = 3, max = 25, message = "category name must be at least between 3 and 35 chars")
    private String name;

    @Size(min = 15, message = "description must be at least 15 chars")
    private String description;
}
