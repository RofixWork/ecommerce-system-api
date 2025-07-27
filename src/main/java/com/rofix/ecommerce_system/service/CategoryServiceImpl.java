package com.rofix.ecommerce_system.service;

import com.rofix.ecommerce_system.dto.request.CategoryRequestDTO;
import com.rofix.ecommerce_system.dto.response.CategoryResponseDTO;
import com.rofix.ecommerce_system.entity.Category;
import com.rofix.ecommerce_system.exception.base.ConflictException;
import com.rofix.ecommerce_system.repository.CategoryRepository;
import com.rofix.ecommerce_system.helpers.EntityHelper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final EntityHelper entityHelper;

    @Override
    public CategoryResponseDTO createCategory(CategoryRequestDTO categoryRequestDTO) {
        validateCategoryNameUniqueness(categoryRequestDTO.getName(), null);

        Category category = modelMapper.map(categoryRequestDTO, Category.class),
                savedCategory = categoryRepository.save(category);

        log.info("Category created successfully");

        return modelMapper.map(savedCategory, CategoryResponseDTO.class);
    }

    @Override
    public List<CategoryResponseDTO> findAll() {
        return categoryRepository.findAll()
                .stream()
                .map(category -> modelMapper.map(category, CategoryResponseDTO.class))
                .toList();
    }

    @Override
    public CategoryResponseDTO findBy(Long categoryId) {
        Category category = entityHelper.getCategoryOrThrow(categoryId);
        return modelMapper.map(category, CategoryResponseDTO.class);
    }

    @Override
    public String deleteCategory(Long categoryId) {
        Category category = entityHelper.getCategoryOrThrow(categoryId);
        categoryRepository.delete(category);
        log.info("Successfully deleted category: {}", category.getName());
        return "Successfully deleted category: " + category.getName() + ".";
    }

    @Transactional
    @Override
    public CategoryResponseDTO updateCategory(Long categoryId, CategoryRequestDTO categoryRequestDTO) {
        validateCategoryNameUniqueness(categoryRequestDTO.getName(), categoryId);
        Category category = entityHelper.getCategoryOrThrow(categoryId);
        category.setName(categoryRequestDTO.getName());
        category.setDescription(categoryRequestDTO.getDescription());
        categoryRepository.save(category);
        log.info("Category with Id: {} updated successfully", categoryId);

        return modelMapper.map(category, CategoryResponseDTO.class);
    }

    private void validateCategoryNameUniqueness(String name, Long categoryId) {
        boolean exist = categoryId == null ?
                categoryRepository.existsByNameIgnoreCase(name)
                : categoryRepository.existsByNameIgnoreCaseAndIdNot(name, categoryId);
        if (exist) {
            log.warn("Category '{}' already exists. Please choose a different one.", name);
            throw new ConflictException("Category '" + name + "' already exists. Please choose a different one.");
        }
    }
}
