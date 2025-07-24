package com.rofix.ecommerce_system.controller;

import com.rofix.ecommerce_system.config.AppConstants;
import com.rofix.ecommerce_system.response.APIResponse;
import com.rofix.ecommerce_system.response.PageListResponse;
import com.rofix.ecommerce_system.security.response.UserInfoResponse;
import com.rofix.ecommerce_system.security.service.UserDetailsImpl;
import com.rofix.ecommerce_system.service.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/users")
    public ResponseEntity<PageListResponse<UserInfoResponse>> getAllUsers(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER)
            @Parameter(description = "Page number (min = 1)") @Min(1) Integer pageNumber,

            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE)
            @Parameter(description = "Page size (min = 1)") @Min(1) Integer pageSize,

            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_BY)
            @Parameter(description = "Field to sort by") String sortBy,

            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_ORDER)
            @Parameter(description = "Sort direction (asc or desc)") String sortOrder
    ) {
        PageListResponse<UserInfoResponse> page = userService.getAllUsers(pageNumber, pageSize, sortBy, sortOrder);

        return ResponseEntity.ok(page);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<UserInfoResponse> getUser(
            @Min(value = 1) @PathVariable Long userId
    ) {
        UserInfoResponse userInfoResponse = userService.getUserBy(userId);
        return ResponseEntity.ok(userInfoResponse);
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<APIResponse> deleteUser(
            @Min(value = 1) @PathVariable Long userId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        String st = userService.deleteUserBy(userId, userDetails);

        return ResponseEntity.ok(new APIResponse(st, true));
    }
}
