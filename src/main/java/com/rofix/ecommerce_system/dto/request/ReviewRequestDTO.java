package com.rofix.ecommerce_system.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequestDTO {
    @NotNull(message = "Product rating is required.")
    @Min(value = 1, message = "Product rating must be between 1 and 5.")
    @Max(value = 5, message = "Product rating must be between 1 and 5.")
    private Integer rating;

    @NotBlank(message = "Comment cannot be empty.")
    @Size(min = 6, message = "Comment must be at least 6 characters long.")
    private String comment;
}
