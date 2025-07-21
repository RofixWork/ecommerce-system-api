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
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    public PageListResponse<ProductResponseDTO> getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, String search, String price) {

        checkProductOrderField(sortBy);
        Pageable pageable = getPageable(pageNumber, pageSize, sortBy, sortOrder);

        String cleanSearch = search != null ? search.trim() : null,
                cleanPrice = price != null ? price.trim() : null;

        //filters
        Page<Product> productPage;

        if (StringUtils.hasText(cleanPrice) && StringUtils.hasText(cleanSearch)) {
            productPage = getPriceFilterOperator(cleanPrice, cleanSearch, pageable);
        } else if (StringUtils.hasText(cleanPrice)) {
            productPage = getPriceFilterOperator(cleanPrice, null, pageable);
        } else if (StringUtils.hasText(cleanSearch)) {
            productPage = productRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(cleanSearch, cleanSearch, pageable);
        } else {
            productPage = productRepository.findAll(pageable);
        }

        List<Product> productList = productPage.getContent();

        List<ProductResponseDTO> productResponseDTOS = productList.stream()
                .map(product -> modelMapper.map(product, ProductResponseDTO.class))
                .toList();

        boolean isEmpty = productPage.isEmpty();
        return new PageListResponse<>(
                productResponseDTOS,
                isEmpty ? 0 : productPage.getSize(),
                isEmpty ? 0 : productPage.getNumber() + 1,
                isEmpty ? 0 : productPage.getTotalPages(),
                isEmpty ? 0 : productPage.getTotalElements(),
                productPage.isLast()
        );
    }

    @Override
    public ProductResponseDTO updateProduct(Long productId, ProductRequestDTO productRequestDTO) {
        Product product = entityHelper.getProductOrThrow(productId);
        Category category = entityHelper.getCategoryOrThrow(productRequestDTO.getCategoryId());

        //update
        product.setName(productRequestDTO.getName());
        product.setDescription(productRequestDTO.getDescription());
        product.setPrice(productRequestDTO.getPrice());
        product.setCategory(category);
        product.setStock(productRequestDTO.getStock());

        product = productRepository.save(product);

        return modelMapper.map(product, ProductResponseDTO.class);
    }

    //------------------HELPERS---------------
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

    private Page<Product> getPriceFilterOperator(String price, String search, Pageable pageable) {

        checkPriceFormat(price);

        String[] priceFilter = price.split(":");
        String operator = priceFilter[0].trim().toLowerCase();
        BigDecimal value = new BigDecimal(priceFilter[1]);

        if (StringUtils.hasText(search) && StringUtils.hasText(price)) {

            return switch (operator) {
                case "eq" -> productRepository.findByNameOrDescriptionAndPriceEquals(search, search, value, pageable);
                case "gt" ->
                        productRepository.findByNameOrDescriptionAndPriceGreaterThan(search, search, value, pageable);
                case "lt" -> productRepository.findByNameOrDescriptionAndPriceLessThan(search, search, value, pageable);
                case "gte" ->
                        productRepository.findByNameOrDescriptionAndPriceGreaterThanOrEqual(search, search, value, pageable);
                case "lte" ->
                        productRepository.findByNameOrDescriptionAndPriceLessThanOrEqual(search, search, value, pageable);
                default -> productRepository.findByNameOrDescriptionAndPriceNotEqual(search, search, value, pageable);
            };
        }

        return switch (operator) {
            case "eq" -> productRepository.findByPriceEquals(value, pageable);
            case "gt" -> productRepository.findByPriceGreaterThan(value, pageable);
            case "lt" -> productRepository.findByPriceLessThan(value, pageable);
            case "gte" -> productRepository.findByPriceGreaterThanEqual(value, pageable);
            case "lte" -> productRepository.findByPriceLessThanEqual(value, pageable);
            default -> productRepository.findByPriceNot(value, pageable);
        };
    }

    private static void checkPriceFormat(String price) {
        Pattern pattern = Pattern.compile("^(eq|lt|gt|neq|lte|gte):\\d{1,12}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(price);

        if (!matcher.find()) {
            throw new BadRequestException("Invalid price format enter like (price=(eq|lt|gt|neq|lte|gte):price)");
        }
    }

    private static void checkProductOrderField(String sortBy) {
        if (!AppConstants.ALLOWED_PRODUCT_ORDERING_FIELDS.contains(sortBy.trim().toLowerCase())) {
            log.error("Invalid sort field entered. Here are the fields you can use: {}", AppConstants.ALLOWED_PRODUCT_ORDERING_FIELDS);
            throw new BadRequestException("Invalid sort field entered. Here are the fields you can use: " + AppConstants.ALLOWED_PRODUCT_ORDERING_FIELDS);
        }
    }

    private Pageable getPageable(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sort = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        return PageRequest.of(pageNumber - 1, pageSize, sort);
    }
}
