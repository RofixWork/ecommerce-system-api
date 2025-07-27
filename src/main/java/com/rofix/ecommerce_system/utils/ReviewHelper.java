package com.rofix.ecommerce_system.utils;


import com.rofix.ecommerce_system.dto.request.ReviewRequestDTO;
import com.rofix.ecommerce_system.entity.Product;
import com.rofix.ecommerce_system.entity.Review;
import com.rofix.ecommerce_system.entity.User;
import com.rofix.ecommerce_system.exception.base.ConflictException;
import com.rofix.ecommerce_system.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;


@Component
@Slf4j
@RequiredArgsConstructor
public class ReviewHelper {
    private final ModelMapper modelMapper;
    private final ReviewRepository reviewRepository;

    public void hasReview(Product product, User user) {
        if (reviewRepository.existsByProductAndUser(product, user)) {
            log.warn("Attempt to create duplicate review for Product ID: {} by User ID: {}",
                    product.getId(), user.getId());

            throw new ConflictException("You have already submitted a review for this product.");
        }
    }

    public Review getSavedReview(ReviewRequestDTO reviewRequestDTO, Product product, User user) {
        Review review = modelMapper.map(reviewRequestDTO, Review.class);
        review.setProduct(product);
        review.setUser(user);
        return reviewRepository.save(review);
    }

}
