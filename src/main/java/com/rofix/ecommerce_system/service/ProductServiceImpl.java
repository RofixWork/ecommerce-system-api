package com.rofix.ecommerce_system.service;

import com.rofix.ecommerce_system.config.AppConstants;
import com.rofix.ecommerce_system.dto.request.ProductRequestDTO;
import com.rofix.ecommerce_system.dto.response.ProductResponseDTO;
import com.rofix.ecommerce_system.entity.Category;
import com.rofix.ecommerce_system.entity.Product;
import com.rofix.ecommerce_system.entity.ProductImage;
import com.rofix.ecommerce_system.repository.ProductRepository;
import com.rofix.ecommerce_system.response.PageListResponse;
import com.rofix.ecommerce_system.security.service.UserDetailsImpl;
import com.rofix.ecommerce_system.helpers.EntityHelper;
import com.rofix.ecommerce_system.helpers.ProductHelper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final EntityHelper entityHelper;
    private final ModelMapper modelMapper;
    private final ProductRepository productRepository;
    private final ProductImageService productImageService;

    private final ProductHelper productHelper;

    @Override
    public ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO, UserDetailsImpl userDetails) {

        productHelper.productNameAlreadyExist(productRequestDTO.getName(), null);

        Category category = entityHelper.getCategoryOrThrow(productRequestDTO.getCategoryId());

        Product savedProduct = productHelper.getSavedProduct(productRequestDTO, userDetails, category);

        log.info("Creating product '{}' in category '{}'", savedProduct.getName(), category.getName());

        return modelMapper.map(savedProduct, ProductResponseDTO.class);
    }

    @Override
    public ProductResponseDTO getProduct(Long productId) {
        return modelMapper.map(entityHelper.getProductOrThrow(productId), ProductResponseDTO.class);
    }

    @Override
    public ProductResponseDTO getProductBySlug(String slug) {
        return modelMapper.map(productHelper.getProductBySlugOrThrow(slug.trim().toLowerCase()), ProductResponseDTO.class);
    }

    @Override
    public PageListResponse<ProductResponseDTO> getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, String search, String price) {

        entityHelper.checkOrderField(sortBy, AppConstants.ALLOWED_PRODUCT_ORDERING_FIELDS);
        Pageable pageable = entityHelper.getPageable(pageNumber, pageSize, sortBy, sortOrder);

        String cleanSearch = search != null ? search.trim() : null,
                cleanPrice = price != null ? price.trim() : null;

        //filters
        Page<Product> productPage;

        if (StringUtils.hasText(cleanPrice) && StringUtils.hasText(cleanSearch)) {
            productPage = productHelper.getPriceFilterOperator(cleanPrice, cleanSearch, pageable);
        } else if (StringUtils.hasText(cleanPrice)) {
            productPage = productHelper.getPriceFilterOperator(cleanPrice, null, pageable);
        } else if (StringUtils.hasText(cleanSearch)) {
            productPage = productRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(cleanSearch, cleanSearch, pageable);
        } else {
            productPage = productRepository.findAll(pageable);
        }

        List<Product> productList = productPage.getContent();

        List<ProductResponseDTO> productResponseDTOS = productList.stream()
                .map(product -> modelMapper.map(product, ProductResponseDTO.class))
                .toList();

        return entityHelper.getPageListResponse(productPage, productResponseDTOS);
    }

    @Override
    public ProductResponseDTO updateProduct(Long productId, ProductRequestDTO productRequestDTO, UserDetailsImpl userDetails) {
        productHelper.productNameAlreadyExist(productRequestDTO.getName(), productId);

        Product product = entityHelper.getProductOrThrow(productId);
        Category category = entityHelper.getCategoryOrThrow(productRequestDTO.getCategoryId());

        productHelper.assertOwnership(userDetails, product);

        //update
        product.setName(productRequestDTO.getName());
        product.setDescription(productRequestDTO.getDescription());
        product.setPrice(productRequestDTO.getPrice());
        product.setCategory(category);
        product.setStock(productRequestDTO.getStock());

        product = productRepository.save(product);

        return modelMapper.map(product, ProductResponseDTO.class);
    }

    @Transactional
    @Override
    public String deleteProduct(Long productId, UserDetailsImpl userDetails) {
        Product product = entityHelper.getProductOrThrow(productId);

        productHelper.assertOwnership(userDetails, product);

        List<ProductImage> productImages = product.getProductImages();

        //no images
        if (productImages == null || productImages.isEmpty()) {
            productRepository.delete(product);
            log.info("Deleted product '{}' from image repository", product.getName());
            return "Product has been deleted successfully";
        }

        for (ProductImage productImage : productImages) {
            productImageService.deleteImageProduct(productId, productImage.getId());
        }
        productRepository.delete(product);
        log.info("Deleted product '{}' and all its images", product.getName());
        return "Product has been deleted successfully";
    }

}
