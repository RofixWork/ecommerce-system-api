package com.rofix.ecommerce_system.service;

import com.rofix.ecommerce_system.dto.request.ReviewRequestDTO;
import com.rofix.ecommerce_system.dto.response.ReviewResponseDTO;
import com.rofix.ecommerce_system.entity.Product;
import com.rofix.ecommerce_system.entity.Review;
import com.rofix.ecommerce_system.entity.User;
import com.rofix.ecommerce_system.exception.base.ConflictException;
import com.rofix.ecommerce_system.exception.base.CustomException;
import com.rofix.ecommerce_system.repository.ReviewRepository;
import com.rofix.ecommerce_system.security.service.UserDetailsImpl;
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
    public ReviewResponseDTO createReview(Long productId, ReviewRequestDTO reviewRequestDTO, UserDetailsImpl userDetails) {
        Product product = entityHelper.getProductOrThrow(productId);
        User user = userDetails.getUser();

        hasReview(product, user);

        Review savedReview = getSavedReview(reviewRequestDTO, product, user);
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

    //    ==================== HELPERS ========================
    private void hasReview(Product product, User user) {
        if (reviewRepository.existsByProductAndUser(product, user)) {
            log.warn("Attempt to create duplicate review for Product ID: {} by User ID: {}",
                    product.getId(), user.getId());

            throw new ConflictException("You have already submitted a review for this product.");
        }
    }

    private Review getSavedReview(ReviewRequestDTO reviewRequestDTO, Product product, User user) {
        Review review = modelMapper.map(reviewRequestDTO, Review.class);
        review.setProduct(product);
        review.setUser(user);
        return reviewRepository.save(review);
    }
}
