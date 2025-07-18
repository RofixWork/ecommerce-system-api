package com.rofix.ecommerce_system.service;

import com.rofix.ecommerce_system.dto.request.ProductRequestDTO;
import com.rofix.ecommerce_system.dto.response.ProductResponseDTO;
import com.rofix.ecommerce_system.entity.Category;
import com.rofix.ecommerce_system.entity.Product;
import com.rofix.ecommerce_system.exception.base.ConflictException;
import com.rofix.ecommerce_system.repository.ProductRepository;
import com.rofix.ecommerce_system.utils.EntityHelper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final EntityHelper entityHelper;
    private final ModelMapper modelMapper;
    private final ProductRepository productRepository;
    private final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);


    @Override
    public ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO) {

        productNameAlreadyExist(productRequestDTO.getName());

        Category category = entityHelper.getCategoryOrThrow(productRequestDTO.getCategoryId());

        Product savedProduct = getSavedProduct(productRequestDTO, category);

        logger.info("Creating product '{}' in category '{}'", savedProduct.getName(), category.getName());

        return modelMapper.map(savedProduct, ProductResponseDTO.class);
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
            logger.warn("Product Name '{}' already exists. Please choose a different one.", name);
            throw new ConflictException("Product '" + name + "' already exists. Please choose a different one.");
        }
    }
}
