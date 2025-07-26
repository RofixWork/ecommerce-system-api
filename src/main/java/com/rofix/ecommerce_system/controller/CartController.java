package com.rofix.ecommerce_system.controller;

import com.rofix.ecommerce_system.config.AppConstants;
import com.rofix.ecommerce_system.dto.request.CartRequestDTO;
import com.rofix.ecommerce_system.dto.response.CartResponseDTO;
import com.rofix.ecommerce_system.response.APIResponse;
import com.rofix.ecommerce_system.response.PageListResponse;
import com.rofix.ecommerce_system.security.service.UserDetailsImpl;
import com.rofix.ecommerce_system.service.CartService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/cart", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @GetMapping
    public ResponseEntity<CartResponseDTO> getCart(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        CartResponseDTO cartPageResponse = cartService.getCartItems(userDetails);

        return ResponseEntity.ok(cartPageResponse);
    }

    @DeleteMapping("/clear")
    public ResponseEntity<APIResponse> clearCart(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(new APIResponse(cartService.clearCart(userDetails), true));
    }

    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<APIResponse> removeProductFromCart(
            @Min(value = 1) @PathVariable Long productId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        String st = cartService.deleteProductFromCart(productId, userDetails);

        return ResponseEntity.ok(new APIResponse(st, true));
    }

    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CartResponseDTO> addProductToCart(
            @Valid @RequestBody CartRequestDTO cartRequestDTO,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        CartResponseDTO cartResponseDTO = cartService.addProductFromCart(cartRequestDTO, userDetails);

        return ResponseEntity.status(HttpStatus.CREATED).body(cartResponseDTO);
    }
}
