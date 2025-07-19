package com.rofix.ecommerce_system.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class ProductRequestDTO {
    @NotBlank(message = "Name cannot be blank!!!")
    @Size(min = 10, max = 200, message = "Name at least between 10 and 200 chars ")
    private String name;

    @NotBlank(message = "Description cannot be blank!!!")
    @Size(min = 60, message = "Description must be at least 60 chars")
    private String description;

    @DecimalMin(value = "0.01", message = "Price must be greater than 0.00")
    @Digits(integer = 12, fraction = 2, message = "Price can have at most 12 integer digits and 2 fractional digits")
    private BigDecimal price;

    @NotNull(message = "Stock cannot be null!!!")
    @Min(value = 1, message = "Minimum stock is 1.")
    private Integer stock;

    @NotNull(message = "Category Id cannot be null!!!")
    @Min(value = 1, message = "Minimum Category Id is 1.")
    private Long categoryId;
}
