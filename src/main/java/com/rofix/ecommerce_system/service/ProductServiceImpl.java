package com.rofix.ecommerce_system.service;

import com.rofix.ecommerce_system.config.AppConstants;
import com.rofix.ecommerce_system.dto.request.ProductRequestDTO;
import com.rofix.ecommerce_system.dto.response.ProductResponseDTO;
import com.rofix.ecommerce_system.entity.Category;
import com.rofix.ecommerce_system.entity.Product;
import com.rofix.ecommerce_system.exception.base.BadRequestException;
import com.rofix.ecommerce_system.exception.base.ConflictException;
import com.rofix.ecommerce_system.exception.base.NotFoundException;
import com.rofix.ecommerce_system.repository.ProductRepository;
import com.rofix.ecommerce_system.response.PageListResponse;
import com.rofix.ecommerce_system.utils.EntityHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public PageListResponse<ProductResponseDTO> getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        checkProductOrderField(sortBy);

        Pageable pageable = getPageable(pageNumber, pageSize, sortBy, sortOrder);
        Page<Product> productPage = productRepository.findAll(pageable);
        List<Product> productList = productPage.getContent();

        List<ProductResponseDTO> getProductResponseDTOS = productList.stream()
                .map(product -> modelMapper.map(product, ProductResponseDTO.class))
                .toList();

        return new PageListResponse<>(
                getProductResponseDTOS,
                productPage.isEmpty() ? 0 : productPage.getSize(),
                productPage.isEmpty() ? 0 : productPage.getNumber() + 1,
                productPage.isEmpty() ? 0 : productPage.getTotalPages(),
                productPage.isEmpty() ? 0 : productPage.getTotalElements(),
                productPage.isLast()
        );
    }

    private static void checkProductOrderField(String sortBy) {
        if (!AppConstants.ALLOWED_PRODUCT_ORDERING_FIELDS.contains(sortBy)) {
            log.error("Invalid sort field entered. Here are the fields you can use: {}", AppConstants.ALLOWED_PRODUCT_ORDERING_FIELDS);
            throw new BadRequestException("Invalid sort field entered. Here are the fields you can use: " + AppConstants.ALLOWED_PRODUCT_ORDERING_FIELDS);
        }
    }

    private Pageable getPageable(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sort = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        return PageRequest.of(pageNumber - 1, pageSize, sort);
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
