package com.rofix.ecommerce_system.service;

import com.rofix.ecommerce_system.dto.request.CategoryRequestDTO;
import com.rofix.ecommerce_system.dto.response.CategoryResponseDTO;
import com.rofix.ecommerce_system.entity.Category;
import com.rofix.ecommerce_system.exception.base.ConflictException;
import com.rofix.ecommerce_system.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Override
    public CategoryResponseDTO createCategory(CategoryRequestDTO categoryRequestDTO) {
        categoryAlreadyExist(categoryRequestDTO.getName());

        Category category = modelMapper.map(categoryRequestDTO, Category.class),
                savedCategory = categoryRepository.save(category);

        logger.info("Category created successfully");

        return modelMapper.map(savedCategory, CategoryResponseDTO.class);
    }

    private void categoryAlreadyExist(String name) {
        if (categoryRepository.existsByNameIgnoreCase(name.trim().toLowerCase())) {
            logger.warn("Category already exists with name {}", name);
            throw new ConflictException("Category '" + name + "' already exists. Please choose a different one.");
        }
    }
}
