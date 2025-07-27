package com.rofix.ecommerce_system.helpers;

import com.rofix.ecommerce_system.entity.*;
import com.rofix.ecommerce_system.exception.base.BadRequestException;
import com.rofix.ecommerce_system.exception.base.NotFoundException;
import com.rofix.ecommerce_system.repository.*;
import com.rofix.ecommerce_system.response.PageListResponse;
import com.rofix.ecommerce_system.security.service.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class EntityHelper {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

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

    public User getUserOrThrow(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> {
            log.info("Product not found with id {}", userId);
            return new NotFoundException("Sorry, that User doesn't exist.");
        });
    }

    public Order getOrderOrThrow(Long orderId, UserDetailsImpl userDetails) {
        User currentUser = userDetails.getUser();
        return orderRepository.findByIdAndUser(orderId, currentUser).orElseThrow(() -> {
            log.warn("Order with ID '{}' not found or not accessible for User ID: '{}'.", orderId, currentUser.getId());
            return new NotFoundException("Sorry, the requested order was not found.");
        });
    }

    public Pageable getPageable(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sort = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        return PageRequest.of(pageNumber - 1, pageSize, sort);
    }

    public <U, T> PageListResponse<T> getPageListResponse(Page<U> page, List<T> content) {
        if (page.isEmpty()) {
            return new PageListResponse<T>(List.of(), 0, 0, 0, 0L, true);
        }

        return new PageListResponse<T>(
                content,
                page.getSize(),
                page.getNumber() + 1,
                page.getTotalPages(),
                page.getTotalElements(),
                page.isLast()
        );
    }

    public void checkOrderField(String sortBy, Set<String> fields) {
        if (!fields.contains(sortBy.trim())) {
            log.error("Invalid sort field entered. Here are the fields you can use: {}", fields);
            throw new BadRequestException("Invalid sort field: '" + sortBy + "'. Allowed fields are: " + fields);
        }
    }
}
