package com.rofix.ecommerce_system.helpers;

import com.rofix.ecommerce_system.dto.request.ProductRequestDTO;
import com.rofix.ecommerce_system.entity.Category;
import com.rofix.ecommerce_system.entity.Product;
import com.rofix.ecommerce_system.exception.base.BadRequestException;
import com.rofix.ecommerce_system.exception.base.ConflictException;
import com.rofix.ecommerce_system.exception.base.NotFoundException;
import com.rofix.ecommerce_system.exception.base.UnauthorizedException;
import com.rofix.ecommerce_system.repository.ProductRepository;
import com.rofix.ecommerce_system.security.service.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductHelper {
    private final ProductRepository productRepository;

    public Product getSavedProduct(ProductRequestDTO productRequestDTO, UserDetailsImpl userDetails, Category category) {
        Product product = new Product(
                productRequestDTO.getName(),
                productRequestDTO.getDescription(),
                productRequestDTO.getPrice(),
                productRequestDTO.getStock(),
                userDetails.getUser(),
                category
        );

        return productRepository.save(product);
    }

    public void productNameAlreadyExist(String name, Long productId) {
        boolean checkProductName = productId == null ?
                productRepository.existsByNameIgnoreCase(name.trim().toLowerCase())
                : productRepository.existsByNameIgnoreCaseAndIdNot(name.trim().toLowerCase(), productId);
        if (checkProductName) {
            log.warn("Product Name '{}' already exists. Please choose a different one.", name);
            throw new ConflictException("Product '" + name + "' already exists. Please choose a different one.");
        }
    }

    public Product getProductBySlugOrThrow(String slug) {
        log.info("Getting Product with Slug {}", slug);
        return productRepository.findBySlugIgnoreCase(slug).orElseThrow(
                () -> {
                    log.info("Product not found with slug {}", slug);
                    return new NotFoundException("Sorry, that product doesn't exist.");
                }
        );
    }

    public Page<Product> getPriceFilterOperator(String price, String search, Pageable pageable) {

        checkPriceFormat(price);

        String[] priceFilter = price.split(":");
        String operator = priceFilter[0].trim().toLowerCase();
        BigDecimal value = new BigDecimal(priceFilter[1]);

        if (StringUtils.hasText(search) && StringUtils.hasText(price)) {

            return switch (operator) {
                case "eq" -> productRepository.findByNameOrDescriptionAndPriceEquals(search, search, value, pageable);
                case "gt" ->
                        productRepository.findByNameOrDescriptionAndPriceGreaterThan(search, search, value, pageable);
                case "lt" -> productRepository.findByNameOrDescriptionAndPriceLessThan(search, search, value, pageable);
                case "gte" ->
                        productRepository.findByNameOrDescriptionAndPriceGreaterThanOrEqual(search, search, value, pageable);
                case "lte" ->
                        productRepository.findByNameOrDescriptionAndPriceLessThanOrEqual(search, search, value, pageable);
                default -> productRepository.findByNameOrDescriptionAndPriceNotEqual(search, search, value, pageable);
            };
        }

        return switch (operator) {
            case "eq" -> productRepository.findByPriceEquals(value, pageable);
            case "gt" -> productRepository.findByPriceGreaterThan(value, pageable);
            case "lt" -> productRepository.findByPriceLessThan(value, pageable);
            case "gte" -> productRepository.findByPriceGreaterThanEqual(value, pageable);
            case "lte" -> productRepository.findByPriceLessThanEqual(value, pageable);
            default -> productRepository.findByPriceNot(value, pageable);
        };
    }

    public void checkPriceFormat(String price) {
        Pattern pattern = Pattern.compile("^(eq|lt|gt|neq|lte|gte):\\d{1,12}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(price);

        if (!matcher.find()) {
            throw new BadRequestException("Invalid price format enter like (price=(eq|lt|gt|neq|lte|gte):price)");
        }
    }

    public void assertOwnership(UserDetailsImpl userDetails, Product product) {
        if (!product.getCreatedBy().getId().equals(userDetails.getId())) {
            log.error("You don't have permission to perform this action.");
            throw new UnauthorizedException("You don't have permission to perform this action.");
        }
    }
}
