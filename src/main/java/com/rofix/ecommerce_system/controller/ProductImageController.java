package com.rofix.ecommerce_system.controller;

import com.rofix.ecommerce_system.dto.response.ProductImageResponseDTO;
import com.rofix.ecommerce_system.response.APIResponse;
import com.rofix.ecommerce_system.service.ProductImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
@Validated
public class ProductImageController {
    private final ProductImageService productImageService;

    public ProductImageController(ProductImageService productImageService) {
        this.productImageService = productImageService;
    }

    @Operation(summary = "Upload an image for a product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Image uploaded successfully",
                    content = @Content(schema = @Schema(implementation = ProductImageResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping(value = "/upload/products/{productId}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductImageResponseDTO> uploadProductImage(
            @Parameter(description = "ID of the product", required = true)
            @Min(value = 1) @PathVariable Long productId,

            @Parameter(description = "Image file to upload", required = true)
            @RequestParam(name = "image") MultipartFile file
    ) {
        ProductImageResponseDTO productImageResponseDTO = productImageService.uploadProductImage(productId, file);
        return ResponseEntity.status(HttpStatus.OK).body(productImageResponseDTO);
    }

    @Operation(summary = "Delete a product image by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Image deleted successfully",
                    content = @Content(schema = @Schema(implementation = APIResponse.class))),
            @ApiResponse(responseCode = "404", description = "Product or Image not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @DeleteMapping("/products/{productId}/image/{productImageId}")
    public ResponseEntity<APIResponse> deleteProductImage(
            @Parameter(description = "ID of the product", required = true)
            @Min(value = 1) @PathVariable Long productId,

            @Parameter(description = "ID of the product image", required = true)
            @Min(value = 1) @PathVariable Long productImageId
    ) {
        return ResponseEntity.ok(new APIResponse(productImageService.deleteImageProduct(productId, productImageId), true));
    }
}
