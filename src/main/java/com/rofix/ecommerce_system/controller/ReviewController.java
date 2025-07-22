package com.rofix.ecommerce_system.controller;

import com.rofix.ecommerce_system.dto.request.ReviewRequestDTO;
import com.rofix.ecommerce_system.dto.response.ReviewResponseDTO;
import com.rofix.ecommerce_system.service.ReviewService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/products", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Product Reviews",
        description = "Endpoints for managing reviews associated with products. " +
                "You can retrieve and create reviews for specific products."
)
public class ReviewController {
    private final ReviewService reviewService;

    @Operation(summary = "Get all reviews for a product", description = "Fetch all reviews associated with a specific product by its ID.")
    @GetMapping("/{productId}/reviews")
    public ResponseEntity<List<ReviewResponseDTO>> getProductReviews(
            @Min(value = 1) @PathVariable Long productId
    ) {
        return ResponseEntity.ok(reviewService.getProductReviews(productId));
    }

    @Operation(summary = "Create a new review for a product", description = "Submit a new review for a product identified by its ID.")
    @PostMapping(value = "/{productId}/reviews", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReviewResponseDTO> createReview(
            @Min(value = 1) @PathVariable Long productId,
            @Valid @RequestBody ReviewRequestDTO reviewRequestDTO
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewService.createReview(productId, reviewRequestDTO));
    }
}
