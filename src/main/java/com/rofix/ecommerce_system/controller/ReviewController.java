package com.rofix.ecommerce_system.controller;

import com.rofix.ecommerce_system.dto.request.ReviewRequestDTO;
import com.rofix.ecommerce_system.dto.response.ReviewResponseDTO;
import com.rofix.ecommerce_system.service.ReviewService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/products", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Validated
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping("/{productId}/reviews")
    public ResponseEntity<ReviewResponseDTO> createReview(
            @Min(value = 1) @PathVariable Long productId,
            @Valid @RequestBody ReviewRequestDTO reviewRequestDTO
    ) {
        ReviewResponseDTO reviewResponseDTO = reviewService.createReview(productId, reviewRequestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(reviewResponseDTO);
    }
}
