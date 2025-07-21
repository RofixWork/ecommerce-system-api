package com.rofix.ecommerce_system.service;

import com.rofix.ecommerce_system.dto.response.ProductImageResponseDTO;
import org.springframework.web.multipart.MultipartFile;

public interface ProductImageService {
    ProductImageResponseDTO uploadProductImage(Long productId, MultipartFile file);
}
