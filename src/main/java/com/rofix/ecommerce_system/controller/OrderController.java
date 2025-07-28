package com.rofix.ecommerce_system.controller;

import com.rofix.ecommerce_system.config.AppConstants;
import com.rofix.ecommerce_system.dto.request.OrderStatusUpdateRequestDTO;
import com.rofix.ecommerce_system.dto.response.OrderResponseDTO;
import com.rofix.ecommerce_system.response.PageListResponse;
import com.rofix.ecommerce_system.security.service.UserDetailsImpl;
import com.rofix.ecommerce_system.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/orders", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "Orders", description = "APIs for managing orders: create, view, and list user orders, All endpoints require the CUSTOMER role.")
@Validated
@PreAuthorize("hasRole('CUSTOMER')")
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "Get all orders for current user", description = "Returns paginated list of user's orders")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PageListResponse.class)))
    })
    @GetMapping
    public ResponseEntity<PageListResponse<OrderResponseDTO>> getAllOrders(
            @Parameter(description = "Page number (starts from 1)", example = "1")
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER) @Min(1) Integer pageNumber,

            @Parameter(description = "Page size", example = "10")
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE) @Min(1) Integer pageSize,

            @Parameter(description = "Sort field (id, totalPrice, createdAt)", example = "createdAt")
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_BY) String sortBy,

            @Parameter(description = "Sort order (asc/desc)", example = "desc")
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_ORDER) String sortOrder,

            @Parameter(hidden = true)
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        PageListResponse<OrderResponseDTO> pageListResponse = orderService.getAllOrders(pageNumber, pageSize, sortBy, sortOrder, userDetails);
        return ResponseEntity.ok(pageListResponse);
    }

    @Operation(summary = "Create new order from cart", description = "Creates a new order using current user's cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input or cart is empty",
                    content = @Content)
    })
    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(
            @Parameter(hidden = true)
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(userDetails));
    }

    @Operation(summary = "Get order by ID", description = "Fetch a specific order by ID (must belong to current user)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Order not found or access denied",
                    content = @Content)
    })
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDTO> getOrder(
            @Parameter(description = "Order ID", example = "1")
            @Min(value = 1) @PathVariable Long orderId,

            @Parameter(hidden = true)
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return ResponseEntity.ok(orderService.getOrderDetails(orderId, userDetails));
    }

    @Operation(
            summary = "Update order status",
            description = "Update the status of an existing order (NEW, PAID, SHIPPED). Only accessible by the authenticated user who owns the order."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order status updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid order ID or status",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Order not found",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized access",
                    content = @Content(mediaType = "application/json"))
    })
    @PatchMapping(value = "/{orderId}/status", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderResponseDTO> updateOrderStatus(
            @Parameter(description = "ID of the order to be updated", example = "1")
            @Min(value = 1) @PathVariable Long orderId,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Order status update payload",
                    required = true,
                    content = @Content(schema = @Schema(implementation = OrderStatusUpdateRequestDTO.class))
            )
            @Valid @RequestBody OrderStatusUpdateRequestDTO orderStatusUpdateRequestDTO,

            @Parameter(hidden = true)
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return ResponseEntity.ok(orderService.updateOrderStatus(orderId, orderStatusUpdateRequestDTO, userDetails));
    }
}