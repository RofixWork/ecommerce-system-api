package com.rofix.ecommerce_system.controller;

import com.rofix.ecommerce_system.dto.response.ProductImageResponseDTO;
import com.rofix.ecommerce_system.response.APIResponse;
import com.rofix.ecommerce_system.service.ProductImageService;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
@Validated
public class ProductImageController {
    private final ProductImageService productImageService;

    public ProductImageController(ProductImageService productImageService) {
        this.productImageService = productImageService;
    }

    @PostMapping("/upload/products/{productId}/image")
    public ResponseEntity<ProductImageResponseDTO> uploadProductImage(
            @Min(value = 1) @PathVariable Long productId,
            @RequestParam(name = "image") MultipartFile file
    ) {
        ProductImageResponseDTO productImageResponseDTO = productImageService.uploadProductImage(productId, file);

        return ResponseEntity.status(HttpStatus.OK).body(productImageResponseDTO);
    }

    @DeleteMapping("/products/{productId}/image/{productImageId}")
    public ResponseEntity<APIResponse> deleteProductImage(
            @Min(value = 1) @PathVariable Long productId,
            @Min(value = 1) @PathVariable Long productImageId
    ) {
        String st = productImageService.deleteImageProduct(productId, productImageId);

        return ResponseEntity.ok(new APIResponse(st, true));
    }
}
