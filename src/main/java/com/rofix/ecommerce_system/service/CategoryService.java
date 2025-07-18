package com.rofix.ecommerce_system.service;

import com.rofix.ecommerce_system.dto.request.CategoryRequestDTO;
import com.rofix.ecommerce_system.dto.response.CategoryResponseDTO;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

import java.util.List;

public interface CategoryService {
    CategoryResponseDTO createCategory(@Valid CategoryRequestDTO categoryRequestDTO);

    List<CategoryResponseDTO> findAll();

    CategoryResponseDTO findBy(Long categoryId);

    String deleteCategory(Long categoryId);

    @Transactional
    CategoryResponseDTO updateCategory(Long categoryId, CategoryRequestDTO categoryRequestDTO);
}