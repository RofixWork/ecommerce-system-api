package com.rofix.ecommerce_system.utils;

import com.rofix.ecommerce_system.entity.Category;
import com.rofix.ecommerce_system.exception.base.NotFoundException;
import com.rofix.ecommerce_system.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EntityHelper {
    private final CategoryRepository categoryRepository;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public Category getCategoryOrThrow(Long categoryId) {
        logger.info("Getting category with id {}", categoryId);
        return categoryRepository.findById(categoryId).orElseThrow(
                () -> {
                    logger.info("Category not found with id {}", categoryId);
                    return new NotFoundException("Sorry, that category doesn't exist.");
                }
        );
    }
}
