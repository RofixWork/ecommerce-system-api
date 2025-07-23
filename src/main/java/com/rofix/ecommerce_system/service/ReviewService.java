package com.rofix.ecommerce_system.service;

import com.rofix.ecommerce_system.dto.request.ReviewRequestDTO;
import com.rofix.ecommerce_system.dto.response.ReviewResponseDTO;
import com.rofix.ecommerce_system.security.service.UserDetailsImpl;

import java.util.List;

public interface ReviewService {
    ReviewResponseDTO createReview(Long productId, ReviewRequestDTO reviewRequestDTO, UserDetailsImpl userDetails);

    List<ReviewResponseDTO> getProductReviews(Long productId);
}
