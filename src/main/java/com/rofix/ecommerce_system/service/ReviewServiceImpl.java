package com.rofix.ecommerce_system.service;

import com.rofix.ecommerce_system.dto.request.ReviewRequestDTO;
import com.rofix.ecommerce_system.dto.response.ReviewResponseDTO;
import com.rofix.ecommerce_system.entity.Product;
import com.rofix.ecommerce_system.entity.Review;
import com.rofix.ecommerce_system.repository.ReviewRepository;
import com.rofix.ecommerce_system.utils.EntityHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final EntityHelper entityHelper;
    private final ReviewRepository reviewRepository;
    private final ModelMapper modelMapper;

    @Override
    public ReviewResponseDTO createReview(Long productId, ReviewRequestDTO reviewRequestDTO) {
        Product product = entityHelper.getProductOrThrow(productId);
        Review review = new Review(
                reviewRequestDTO.getRating(),
                reviewRequestDTO.getComment().trim().toLowerCase(),
                product
        ),
                savedReview = reviewRepository.save(review);
        log.info("Review created: {}", savedReview);
        return modelMapper.map(savedReview, ReviewResponseDTO.class);
    }

    @Override
    public List<ReviewResponseDTO> getProductReviews(Long productId) {
        Product product = entityHelper.getProductOrThrow(productId);
        List<Review> reviews = reviewRepository.findByProductOrderByCreatedAtDesc(product);

        log.info("Fetching reviews for product {}", product.getName());

        return reviews.stream()
                .map(review -> modelMapper.map(review, ReviewResponseDTO.class))
                .collect(Collectors.toList());
    }
}
