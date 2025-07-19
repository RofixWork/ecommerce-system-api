package com.rofix.ecommerce_system.repository;

import com.rofix.ecommerce_system.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsByNameIgnoreCase(String name);

    Optional<Product> findBySlugIgnoreCase(String slug);
}
