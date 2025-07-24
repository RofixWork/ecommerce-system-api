package com.rofix.ecommerce_system.controller;

import com.rofix.ecommerce_system.dto.request.CartRequestDTO;
import com.rofix.ecommerce_system.dto.response.CartResponseDTO;
import com.rofix.ecommerce_system.security.service.UserDetailsImpl;
import com.rofix.ecommerce_system.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/cart", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CartResponseDTO> addProductToCart(
            @Valid @RequestBody CartRequestDTO cartRequestDTO,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        CartResponseDTO cartResponseDTO = cartService.addProductFromCart(cartRequestDTO, userDetails);

        return ResponseEntity.status(HttpStatus.CREATED).body(cartResponseDTO);
    }
}
