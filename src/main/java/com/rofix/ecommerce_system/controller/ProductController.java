package com.rofix.ecommerce_system.controller;

import com.rofix.ecommerce_system.config.AppConstants;
import com.rofix.ecommerce_system.dto.request.ProductRequestDTO;
import com.rofix.ecommerce_system.dto.response.ProductResponseDTO;
import com.rofix.ecommerce_system.entity.Product;
import com.rofix.ecommerce_system.response.PageListResponse;
import com.rofix.ecommerce_system.service.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Validated
public class ProductController {
    private final ProductService productService;

    @PostMapping("/products")
    public ResponseEntity<ProductResponseDTO> createProduct(
            @Valid @RequestBody ProductRequestDTO productRequestDTO

    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(productRequestDTO));
    }

    @GetMapping("/products")
    public ResponseEntity<PageListResponse<ProductResponseDTO>> getAllProducts(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER) @Min(value = 1) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE) @Min(value = 1) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_BY) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_ORDER) String sortOrder,
            @RequestParam(name = "search", required = false) String search,
            @RequestParam(name = "price", required = false) String price
    ) {
        PageListResponse<ProductResponseDTO> pageListResponse = productService.getAllProducts(pageNumber, pageSize, sortBy, sortOrder, search, price);

        return ResponseEntity.ok(pageListResponse);
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<ProductResponseDTO> getProductById(
            @PathVariable Long productId
    ) {
        return ResponseEntity.ok(productService.getProduct(productId));
    }

    @GetMapping("/products/slug/{slug}")
    public ResponseEntity<ProductResponseDTO> getProductBySlug(
            @PathVariable String slug
    ) {
        return ResponseEntity.ok(productService.getProductBySlug(slug));
    }
}
