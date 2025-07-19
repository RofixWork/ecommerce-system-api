package com.rofix.ecommerce_system.service;

import com.rofix.ecommerce_system.dto.request.ProductRequestDTO;
import com.rofix.ecommerce_system.dto.response.ProductResponseDTO;
import com.rofix.ecommerce_system.entity.Category;
import com.rofix.ecommerce_system.entity.Product;
import com.rofix.ecommerce_system.exception.base.ConflictException;
import com.rofix.ecommerce_system.exception.base.NotFoundException;
import com.rofix.ecommerce_system.repository.ProductRepository;
import com.rofix.ecommerce_system.utils.EntityHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final EntityHelper entityHelper;
    private final ModelMapper modelMapper;
    private final ProductRepository productRepository;


    @Override
    public ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO) {

        productNameAlreadyExist(productRequestDTO.getName());

        Category category = entityHelper.getCategoryOrThrow(productRequestDTO.getCategoryId());

        Product savedProduct = getSavedProduct(productRequestDTO, category);

        log.info("Creating product '{}' in category '{}'", savedProduct.getName(), category.getName());

        return modelMapper.map(savedProduct, ProductResponseDTO.class);
    }

    @Override
    public ProductResponseDTO getProduct(Long productId) {
        return modelMapper.map(entityHelper.getProductOrThrow(productId), ProductResponseDTO.class);
    }

    @Override
    public ProductResponseDTO getProductBySlug(String slug) {
        return modelMapper.map(getProductBySlugOrThrow(slug.trim().toLowerCase()), ProductResponseDTO.class);
    }

    private Product getSavedProduct(ProductRequestDTO productRequestDTO, Category category) {
        Product product = new Product(
                productRequestDTO.getName(),
                productRequestDTO.getDescription(),
                productRequestDTO.getPrice(),
                productRequestDTO.getStock(),
                category
        );

        return productRepository.save(product);
    }

    private void productNameAlreadyExist(String name) {
        if (productRepository.existsByNameIgnoreCase(name.trim().toLowerCase())) {
            log.warn("Product Name '{}' already exists. Please choose a different one.", name);
            throw new ConflictException("Product '" + name + "' already exists. Please choose a different one.");
        }
    }

    private Product getProductBySlugOrThrow(String slug) {
        log.info("Getting Product with Slug {}", slug);
        return productRepository.findBySlugIgnoreCase(slug).orElseThrow(
                () -> {
                    log.info("Product not found with slug {}", slug);
                    return new NotFoundException("Sorry, that product doesn't exist.");
                }
        );
    }
}
