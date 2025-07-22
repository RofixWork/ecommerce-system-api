package com.rofix.ecommerce_system.controller;

import com.rofix.ecommerce_system.response.APIResponse;
import com.rofix.ecommerce_system.security.request.RegisterRequest;
import com.rofix.ecommerce_system.security.response.UserInfoResponse;
import com.rofix.ecommerce_system.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/auth", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<APIResponse> register(
            @Valid @RequestBody RegisterRequest registerRequest
    ) {
        String status = authService.register(registerRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(new APIResponse(status, true));
    }
}
