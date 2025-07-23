package com.rofix.ecommerce_system.service;

import com.rofix.ecommerce_system.dto.request.ProductRequestDTO;
import com.rofix.ecommerce_system.dto.response.ProductResponseDTO;
import com.rofix.ecommerce_system.response.PageListResponse;
import com.rofix.ecommerce_system.security.service.UserDetailsImpl;
import jakarta.transaction.Transactional;

public interface ProductService {
    ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO, UserDetailsImpl userDetails);

    ProductResponseDTO getProduct(Long productId);

    ProductResponseDTO getProductBySlug(String slug);

    PageListResponse<ProductResponseDTO> getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, String search, String price);

    ProductResponseDTO updateProduct(Long productId, ProductRequestDTO productRequestDTO, UserDetailsImpl userDetails);

    @Transactional
    String deleteProduct(Long productId);
}
