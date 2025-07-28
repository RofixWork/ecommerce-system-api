package com.rofix.ecommerce_system.controller;

import com.rofix.ecommerce_system.dto.request.CartRequestDTO;
import com.rofix.ecommerce_system.dto.response.CartResponseDTO;
import com.rofix.ecommerce_system.response.APIResponse;
import com.rofix.ecommerce_system.security.service.UserDetailsImpl;
import com.rofix.ecommerce_system.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/cart", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "Cart Controller", description = "APIs for managing the shopping cart, All endpoints require the CUSTOMER role.")
@PreAuthorize("hasRole('CUSTOMER')")
public class CartController {
    private final CartService cartService;

    @Operation(summary = "Get Cart Items", description = "Retrieve all products in the current user's cart.")
    @ApiResponse(responseCode = "200", description = "Cart retrieved successfully")
    @GetMapping
    public ResponseEntity<CartResponseDTO> getCart(
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        CartResponseDTO cartPageResponse = cartService.getCartItems(userDetails);
        return ResponseEntity.ok(cartPageResponse);
    }

    @Operation(summary = "Clear Cart", description = "Remove all products from the current user's cart.")
    @ApiResponse(responseCode = "200", description = "Cart cleared successfully")
    @DeleteMapping("/clear")
    public ResponseEntity<APIResponse> clearCart(
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return ResponseEntity.ok(new APIResponse(cartService.clearCart(userDetails), true));
    }

    @Operation(summary = "Remove Product from Cart", description = "Remove a specific product from the user's cart.")
    @ApiResponse(responseCode = "200", description = "Product removed successfully")
    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<APIResponse> removeProductFromCart(
            @Parameter(description = "ID of the product to be removed", required = true)
            @Min(value = 1) @PathVariable Long productId,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        String st = cartService.deleteProductFromCart(productId, userDetails);
        return ResponseEntity.ok(new APIResponse(st, true));
    }

    @Operation(summary = "Add Product to Cart", description = "Add a product to the user's cart.")
    @ApiResponse(responseCode = "201", description = "Product added successfully")
    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CartResponseDTO> addProductToCart(
            @Parameter(description = "Product and quantity to add", required = true)
            @Valid @RequestBody CartRequestDTO cartRequestDTO,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        CartResponseDTO cartResponseDTO = cartService.addProductFromCart(cartRequestDTO, userDetails);
        return ResponseEntity.status(HttpStatus.CREATED).body(cartResponseDTO);
    }
}
