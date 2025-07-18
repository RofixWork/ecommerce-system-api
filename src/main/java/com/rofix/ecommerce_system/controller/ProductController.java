package com.rofix.ecommerce_system.controller;

import com.rofix.ecommerce_system.dto.request.ProductRequestDTO;
import com.rofix.ecommerce_system.dto.response.ProductResponseDTO;
import com.rofix.ecommerce_system.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping("/products")
    public ResponseEntity<ProductResponseDTO> createProduct(
            @Valid @RequestBody ProductRequestDTO productRequestDTO

    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(productRequestDTO));
    }
}
