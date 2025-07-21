package com.rofix.ecommerce_system.service;

import com.rofix.ecommerce_system.dto.request.ProductRequestDTO;
import com.rofix.ecommerce_system.dto.response.ProductResponseDTO;
import com.rofix.ecommerce_system.response.PageListResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

public interface ProductService {
    ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO);

    ProductResponseDTO getProduct(Long productId);

    ProductResponseDTO getProductBySlug(String slug);

    PageListResponse<ProductResponseDTO> getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, String search, String price);

    ProductResponseDTO updateProduct(Long productId, ProductRequestDTO productRequestDTO);
}
