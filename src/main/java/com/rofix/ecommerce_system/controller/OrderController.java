package com.rofix.ecommerce_system.controller;

import com.rofix.ecommerce_system.config.AppConstants;
import com.rofix.ecommerce_system.dto.response.OrderItemResponseDTO;
import com.rofix.ecommerce_system.dto.response.OrderResponseDTO;
import com.rofix.ecommerce_system.entity.OrderItem;
import com.rofix.ecommerce_system.response.PageListResponse;
import com.rofix.ecommerce_system.security.service.UserDetailsImpl;
import com.rofix.ecommerce_system.service.OrderService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/orders", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<PageListResponse<OrderResponseDTO>> getAllOrders(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER) @Min(1) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE) @Min(1) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_BY) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_ORDER) String sortOrder,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        PageListResponse<OrderResponseDTO> pageListResponse = orderService.getAllOrders(pageNumber, pageSize, sortBy, sortOrder, userDetails);

        return ResponseEntity.ok(pageListResponse);
    }

    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(userDetails));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDTO> getOrder(@Min(value = 1) @PathVariable Long orderId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(orderService.getOrderDetails(orderId, userDetails));
    }
}
