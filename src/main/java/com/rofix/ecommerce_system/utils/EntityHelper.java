package com.rofix.ecommerce_system.utils;

import com.rofix.ecommerce_system.entity.Category;
import com.rofix.ecommerce_system.entity.Product;
import com.rofix.ecommerce_system.entity.ProductImage;
import com.rofix.ecommerce_system.exception.base.NotFoundException;
import com.rofix.ecommerce_system.repository.CategoryRepository;
import com.rofix.ecommerce_system.repository.ProductImageRepository;
import com.rofix.ecommerce_system.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EntityHelper {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;

    public Category getCategoryOrThrow(Long categoryId) {
        log.info("Getting category with id {}", categoryId);
        return categoryRepository.findById(categoryId).orElseThrow(
                () -> {
                    log.info("Category not found with id {}", categoryId);
                    return new NotFoundException("Sorry, that category doesn't exist.");
                }
        );
    }

    public Product getProductOrThrow(Long productId) {
        log.info("Getting Product with id {}", productId);
        return productRepository.findById(productId).orElseThrow(
                () -> {
                    log.info("Product not found with id {}", productId);
                    return new NotFoundException("Sorry, that product doesn't exist.");
                }
        );
    }

    public ProductImage getProductImageOrThrow(Long productImageId) {
        return productImageRepository.findById(productImageId).orElseThrow(() -> {
            log.info("Product not found with id {}", productImageId);
            return new NotFoundException("Sorry, that Product Image doesn't exist.");
        });
    }
}
