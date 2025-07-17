package com.rofix.ecommerce_system.service;

import com.rofix.ecommerce_system.dto.request.CategoryRequestDTO;
import com.rofix.ecommerce_system.dto.response.CategoryResponseDTO;
import jakarta.validation.Valid;

public interface CategoryService {
    CategoryResponseDTO createCategory(@Valid CategoryRequestDTO categoryRequestDTO);
}