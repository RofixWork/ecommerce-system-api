package com.rofix.ecommerce_system.controller;

import com.rofix.ecommerce_system.dto.response.ProductImageResponseDTO;
import com.rofix.ecommerce_system.service.ProductImageService;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/upload")
@Validated
public class ProductImageController {
    private final ProductImageService productImageService;

    public ProductImageController(ProductImageService productImageService) {
        this.productImageService = productImageService;
    }

    @PostMapping("/products/{productId}/image")
    public ResponseEntity<ProductImageResponseDTO> uploadProductImage(
            @Min(value = 1) @PathVariable Long productId,
            @RequestParam(name = "image") MultipartFile file
    ) {
        ProductImageResponseDTO productImageResponseDTO = productImageService.uploadProductImage(productId, file);

        return ResponseEntity.status(HttpStatus.OK).body(productImageResponseDTO);
    }
}
