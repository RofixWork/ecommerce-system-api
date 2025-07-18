package com.rofix.ecommerce_system.service;

import com.rofix.ecommerce_system.dto.request.ProductRequestDTO;
import com.rofix.ecommerce_system.dto.response.ProductResponseDTO;
import jakarta.validation.Valid;

public interface ProductService {
    ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO);
}
