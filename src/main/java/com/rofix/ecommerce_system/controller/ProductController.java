package com.rofix.ecommerce_system.controller;

import com.rofix.ecommerce_system.config.AppConstants;
import com.rofix.ecommerce_system.dto.request.ProductRequestDTO;
import com.rofix.ecommerce_system.dto.response.ProductResponseDTO;
import com.rofix.ecommerce_system.response.APIResponse;
import com.rofix.ecommerce_system.response.PageListResponse;
import com.rofix.ecommerce_system.security.service.UserDetailsImpl;
import com.rofix.ecommerce_system.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Validated
@Tag(name = "Product", description = "APIs for managing products")
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "Create new product")
    @ApiResponse(responseCode = "201", description = "Product created successfully")
    @PostMapping(value = "/products", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductResponseDTO> createProduct(
            @Valid @RequestBody ProductRequestDTO productRequestDTO,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(productRequestDTO, userDetails));
    }

    @Operation(summary = "Get all products with pagination, sorting and filtering")
    @GetMapping("/products")
    public ResponseEntity<PageListResponse<ProductResponseDTO>> getAllProducts(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER)
            @Parameter(description = "Page number (min = 1)") @Min(1) Integer pageNumber,

            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE)
            @Parameter(description = "Page size (min = 1)") @Min(1) Integer pageSize,

            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_BY)
            @Parameter(description = "Field to sort by") String sortBy,

            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_ORDER)
            @Parameter(description = "Sort direction (asc or desc)") String sortOrder,

            @RequestParam(name = "search", required = false)
            @Parameter(description = "Search keyword") String search,

            @RequestParam(name = "price", required = false)
            @Parameter(description = "Price filter format: min-max") String price
    ) {
        PageListResponse<ProductResponseDTO> pageListResponse = productService.getAllProducts(pageNumber, pageSize, sortBy, sortOrder, search, price);
        return ResponseEntity.ok(pageListResponse);
    }

    @Operation(summary = "Get product by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product found"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @GetMapping("/products/{productId}")
    public ResponseEntity<ProductResponseDTO> getProductById(
            @PathVariable @Min(1) Long productId
    ) {
        return ResponseEntity.ok(productService.getProduct(productId));
    }

    @Operation(summary = "Get product by slug")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product found"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @GetMapping("/products/slug/{slug}")
    public ResponseEntity<ProductResponseDTO> getProductBySlug(
            @PathVariable String slug
    ) {
        return ResponseEntity.ok(productService.getProductBySlug(slug));
    }

    @Operation(summary = "Update product by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product updated successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @PutMapping(value = "/products/{productId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductResponseDTO> updateProduct(
            @PathVariable @Min(1) Long productId,
            @Valid @RequestBody ProductRequestDTO productRequestDTO
    ) {
        return ResponseEntity.ok(productService.updateProduct(productId, productRequestDTO));
    }

    @Operation(summary = "Delete product by ID with all associated images")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @DeleteMapping("/products/{productId}")
    public ResponseEntity<APIResponse> deleteProduct(
            @PathVariable @Min(1) Long productId
    ) {
        return ResponseEntity.ok(new APIResponse(productService.deleteProduct(productId), true));
    }
}
