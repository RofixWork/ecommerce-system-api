package com.rofix.ecommerce_system.service;

import com.rofix.ecommerce_system.dto.response.ProductImageResponseDTO;
import jakarta.validation.constraints.Min;
import org.springframework.web.multipart.MultipartFile;

public interface ProductImageService {
    ProductImageResponseDTO uploadProductImage(Long productId, MultipartFile file);

    String deleteImageProduct(Long productId, Long productImageId);
}
