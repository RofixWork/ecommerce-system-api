package com.rofix.ecommerce_system.service;

import com.rofix.ecommerce_system.dto.request.CategoryRequestDTO;
import com.rofix.ecommerce_system.dto.response.CategoryResponseDTO;
import com.rofix.ecommerce_system.entity.Category;
import com.rofix.ecommerce_system.exception.base.ConflictException;
import com.rofix.ecommerce_system.exception.base.NotFoundException;
import com.rofix.ecommerce_system.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Override
    public CategoryResponseDTO createCategory(CategoryRequestDTO categoryRequestDTO) {
        validateCategoryNameUniqueness(categoryRequestDTO.getName(), null);

        Category category = modelMapper.map(categoryRequestDTO, Category.class),
                savedCategory = categoryRepository.save(category);

        logger.info("Category created successfully");

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
        Category category = getCategoryOrThrow(categoryId);
        return modelMapper.map(category, CategoryResponseDTO.class);
    }

    @Override
    public String deleteCategory(Long categoryId) {
        Category category = getCategoryOrThrow(categoryId);
        categoryRepository.delete(category);
        logger.info("Successfully deleted category: {}", category.getName());
        return "Successfully deleted category: " + category.getName() + ".";
    }

    @Transactional
    @Override
    public CategoryResponseDTO updateCategory(Long categoryId, CategoryRequestDTO categoryRequestDTO) {
        validateCategoryNameUniqueness(categoryRequestDTO.getName(), categoryId);
        Category category = getCategoryOrThrow(categoryId);
        category.setName(categoryRequestDTO.getName());
        category.setDescription(categoryRequestDTO.getDescription());
        categoryRepository.save(category);
        logger.info("Category with Id: {} updated successfully", categoryId);

        return modelMapper.map(category, CategoryResponseDTO.class);
    }

    private void validateCategoryNameUniqueness(String name, Long categoryId) {
        boolean exist = categoryId == null ?
                categoryRepository.existsByNameIgnoreCase(name)
                : categoryRepository.existsByNameIgnoreCaseAndIdNot(name, categoryId);
        if (exist) {
            logger.warn("Category '{}' already exists. Please choose a different one.", name);
            throw new ConflictException("Category '" + name + "' already exists. Please choose a different one.");
        }
    }

    private Category getCategoryOrThrow(Long categoryId) {
        logger.info("Getting category with id {}", categoryId);
        return categoryRepository.findById(categoryId).orElseThrow(
                () -> {
                    logger.info("Category not found with id {}", categoryId);
                    return new NotFoundException("Sorry, that category doesn't exist.");
                }
        );
    }
}
