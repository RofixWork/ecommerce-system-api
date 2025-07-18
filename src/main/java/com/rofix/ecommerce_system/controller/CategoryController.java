package com.rofix.ecommerce_system.controller;

import com.rofix.ecommerce_system.dto.request.CategoryRequestDTO;
import com.rofix.ecommerce_system.dto.response.CategoryResponseDTO;
import com.rofix.ecommerce_system.response.APIResponse;
import com.rofix.ecommerce_system.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Validated
@Tag(name = "Category", description = "APIs for managing product categories")
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "Create a new category",
            description = "Creates a new product category using provided data.")
    @ApiResponse(responseCode = "201", description = "Category created successfully",
            content = @Content(schema = @Schema(implementation = CategoryResponseDTO.class)))
    @PostMapping("/categories")
    public ResponseEntity<CategoryResponseDTO> createCategory(
            @Valid @RequestBody CategoryRequestDTO categoryRequestDTO
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.createCategory(categoryRequestDTO));
    }

    @Operation(summary = "Get all categories",
            description = "Returns a list of all categories.")
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(schema = @Schema(implementation = CategoryResponseDTO.class)))
    @GetMapping("/categories")
    public ResponseEntity<List<CategoryResponseDTO>> getAllCategories() {
        return ResponseEntity.ok(categoryService.findAll());
    }

    @Operation(summary = "Get category by ID",
            description = "Returns a single category by its ID.")
    @ApiResponse(responseCode = "200", description = "Category found",
            content = @Content(schema = @Schema(implementation = CategoryResponseDTO.class)))
    @ApiResponse(responseCode = "404", description = "Category not found")
    @GetMapping("/categories/{categoryId}")
    public ResponseEntity<CategoryResponseDTO> getCategory(
            @Parameter(description = "ID of the category to retrieve", example = "1")
            @Min(1) @PathVariable Long categoryId
    ) {
        return ResponseEntity.ok(categoryService.findBy(categoryId));
    }

    @Operation(summary = "Update category",
            description = "Updates an existing category using its ID.")
    @ApiResponse(responseCode = "200", description = "Category updated",
            content = @Content(schema = @Schema(implementation = CategoryResponseDTO.class)))
    @ApiResponse(responseCode = "404", description = "Category not found")
    @PutMapping("/categories/{categoryId}")
    public ResponseEntity<CategoryResponseDTO> updateCategory(
            @Parameter(description = "ID of the category to update", example = "1")
            @Min(1) @PathVariable Long categoryId,
            @Valid @RequestBody CategoryRequestDTO categoryRequestDTO
    ) {
        return ResponseEntity.ok(categoryService.updateCategory(categoryId, categoryRequestDTO));
    }

    @Operation(summary = "Delete category",
            description = "Deletes a category by its ID.")
    @ApiResponse(responseCode = "200", description = "Category deleted",
            content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    @ApiResponse(responseCode = "404", description = "Category not found")
    @DeleteMapping("/categories/{categoryId}")
    public ResponseEntity<APIResponse> deleteCategory(
            @Parameter(description = "ID of the category to delete", example = "1")
            @Min(1) @PathVariable Long categoryId
    ) {
        return ResponseEntity.ok(new APIResponse(categoryService.deleteCategory(categoryId), true));
    }
}
