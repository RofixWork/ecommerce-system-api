package com.rofix.ecommerce_system.repository;

import com.rofix.ecommerce_system.entity.Product;
import com.rofix.ecommerce_system.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByProductOrderByCreatedAtDesc(Product product);
}
